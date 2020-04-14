package ace.fw.mq.rocketmq.impl.producer;

import ace.fw.model.response.GenericResponseExt;
import ace.fw.mq.enums.MqErrorEnum;
import ace.fw.mq.enums.TransactionStatusEnum;
import ace.fw.mq.model.MessageContext;
import ace.fw.mq.model.TransactionMessage;
import ace.fw.mq.producer.TransactionMQChecker;
import ace.fw.mq.producer.TransactionMQProducer;
import ace.fw.mq.rocketmq.property.RocketMQTransactionExecutorServiceProperty;
import ace.fw.mq.rocketmq.util.LogUtils;
import ace.fw.mq.serializer.Deserializer;
import ace.fw.mq.serializer.Serializer;
import ace.fw.util.GenericResponseExtUtils;
import ace.fw.util.ReflectionUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 10:30
 * @description
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Slf4j
public class TransactionMQProducerImpl
        implements TransactionMQProducer, InitializingBean, DisposableBean {
    /**
     * 底层事务MQ生产者实现
     */
    private org.apache.rocketmq.client.producer.TransactionMQProducer transactionMQProducer;
    /**
     * 序列化工具
     */
    private Serializer defaultSerializer;
    /**
     * 反序列化工具
     */
    private Deserializer defaultDeserializer;
    /**
     * 本地与回查事务处理器
     */
    private TransactionMQChecker transactionMqChecker;
    /**
     * rocketmq 消息限制检查器
     */
    private RocketMQMessageChecker rocketMQMessageChecker;
    /**
     * rocketmq 消息转换器
     */
    private MessageConverter messageConverter;
    /**
     * rocketmq 名称服务器地址 ,如172.18.0.1:9876
     */
    private String nameServerAddress;
    /**
     * 生产者组名
     */
    private String groupName;
    /**
     * 回查监听线程配置
     */
    private RocketMQTransactionExecutorServiceProperty rocketMQTransactionExecutorServiceProperty;
    /**
     * 用于标识该实现类，主要用于日志记录
     */
    private String transactionMQProducerId;
    /**
     * 消息体的类类型
     */
    private Class messageBodyClass;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.transactionMQProducerId, "transactionMQProducerId can be null");
        Assert.notNull(this.defaultDeserializer, "defaultDeserializer can be null");
        Assert.notNull(this.defaultSerializer, "defaultSerializer can be null");
        Assert.notNull(this.groupName, "groupName can be null");
        Assert.notNull(this.messageConverter, "messageConverter can be null");
        Assert.notNull(this.nameServerAddress, "nameServerAddress can be null");
        Assert.notNull(this.rocketMQMessageChecker, "rocketMQMessageChecker can be null");
        Assert.notNull(this.transactionMqChecker, "transactionMqChecker can be null");
        messageBodyClass = ReflectionUtils.getInterfaceGeneric(this.transactionMqChecker.getClass(), 0);
        this.transactionMQProducer = this.createMQProducer();
        this.transactionMQProducer.start();
    }

    private org.apache.rocketmq.client.producer.TransactionMQProducer createMQProducer() {
        org.apache.rocketmq.client.producer.TransactionMQProducer transactionMQProducer = new org.apache.rocketmq.client.producer.TransactionMQProducer(groupName);
        TransactionListener transactionListener = this.createTransactionListener();
        ExecutorService executorService = this.createExecutorService();
        transactionMQProducer.setTransactionListener(transactionListener);
        transactionMQProducer.setExecutorService(executorService);
        return transactionMQProducer;
    }

    private ExecutorService createExecutorService() {
        RocketMQTransactionExecutorServiceProperty property = this.rocketMQTransactionExecutorServiceProperty;
        ThreadFactory threadFactory = r -> {
            Thread thread = new Thread(r);
            thread.setName(this.transactionMQProducerId + "-transaction-msg-check-thread");
            return thread;
        };

        ExecutorService executorService = new ThreadPoolExecutor(property.getCorePoolSize(),
                property.getMaximumPoolSize(),
                property.getKeepAliveTime(),
                property.getTimeUnit(),
                new ArrayBlockingQueue<Runnable>(property.getQueueLength()),
                threadFactory
        );
        return executorService;
    }

    private TransactionListener createTransactionListener() {
        return new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                return TransactionMQProducerImpl.this.executeLocalTransaction(msg, arg);
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                return TransactionMQProducerImpl.this.checkLocalTransaction(msg);
            }
        };
    }

    private LocalTransactionState checkLocalTransaction(MessageExt messageExt) {

        List<String> tags = this.messageConverter.toTagListFromTags(messageExt.getTags());

        Object messageBody;
        try {
            messageBody = this.deserialize(messageExt);
        } catch (Exception ex) {
            String errorMsg = String.format("反序列化消息内容失败.%s", LogUtils.getLogFromMessage(messageExt));
            log.error(errorMsg, ex);
            throw ex;
        }

        MessageContext messageContext = MessageContext.builder()
                .extProperties(messageExt.getProperties())
                .reconsumeCount(messageExt.getReconsumeTimes())
                .topic(messageExt.getTopic())
                .messageBody(messageBody)
                .tags(tags)
                .build();
        try {
            TransactionStatusEnum transactionStatusEnum = this.transactionMqChecker.checkLocalTransaction(messageContext);
            return TransactionStatusConverter.toRocketMQTransactionState(transactionStatusEnum);
        } catch (Exception ex) {
            log.error("事务回查事件执行失败." + LogUtils.getLogFromMessage(messageExt), ex);
        }
        return LocalTransactionState.UNKNOW;
    }

    private LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        TransactionMessageInternalParams transactionMessageInternalParams = (TransactionMessageInternalParams) arg;
        TransactionMessage message = transactionMessageInternalParams.getMessage();
        Object bizParams = transactionMessageInternalParams.getBizParams();
        LocalTransactionState localTransactionState = LocalTransactionState.UNKNOW;
        try {
            TransactionStatusEnum transactionStatusEnum = this.transactionMqChecker.executeLocalTransaction(message, bizParams);
            return TransactionStatusConverter.toRocketMQTransactionState(transactionStatusEnum);
        } catch (Exception ex) {
            String errorMsg = String.format("本地事务方法执行失败.%s,%s", LogUtils.getLogFromMessage(msg), LogUtils.toJson(arg));
            log.error(errorMsg, ex);
        }
        return localTransactionState;
    }

    @Override
    public void destroy() {
        if (Objects.nonNull(this.transactionMQProducer)) {
            this.transactionMQProducer.shutdown();
        }
    }


    @Override
    public GenericResponseExt<TransactionStatusEnum> send(TransactionMessage message, Object args) {
        val rocketMQMessage = this.getMessageConverter().toRocketMQMessage(message, this.getDefaultSerializer());

        GenericResponseExt<Boolean> checkRocketMQMessageResult = this.getRocketMQMessageChecker().check(rocketMQMessage);
        if (checkRocketMQMessageResult.getSuccess() == false) {
            GenericResponseExt<TransactionStatusEnum> result = new GenericResponseExt<TransactionStatusEnum>();
            result.setData(TransactionStatusEnum.UNKNOWN);
            result.setCode(checkRocketMQMessageResult.getCode());
            result.setMessage(checkRocketMQMessageResult.getMessage());
            return result;
        }

        try {
            TransactionMessageInternalParams transactionMessageInternalParams = TransactionMessageInternalParams.builder()
                    .bizParams(args)
                    .message(message)
                    .build();

            TransactionSendResult sendResult = this.getTransactionMQProducer().sendMessageInTransaction(rocketMQMessage, transactionMessageInternalParams);

            if (SendStatus.SEND_OK.equals(sendResult.getSendStatus()) == false) {
                GenericResponseExt<TransactionStatusEnum> result = new GenericResponseExt<TransactionStatusEnum>();
                result.setData(TransactionStatusEnum.UNKNOWN);
                result.setCode(MqErrorEnum.MQ_EXCEPTION.getCode());
                result.setMessage(MqErrorEnum.MQ_EXCEPTION.getDesc());
                return result;
            }
            val transactionStatusResult = TransactionStatusConverter.toTransactionStatus(sendResult.getLocalTransactionState());
            return GenericResponseExtUtils.buildSuccessWithData(transactionStatusResult);
        } catch (Exception ex) {
            List<String> tags = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(message.getTags())) {
                tags.addAll(message.getTags());
            }
            String msg = String.format("[事务消息发送失败],[topic:%s-%s-%s]",
                    message.getTopic().getCode(),
                    message.getTopic().getDesc(),
                    String.join(",", tags));
            log.error(msg, ex);
        }
        return GenericResponseExtUtils.buildWithDataAndCodeEnum(TransactionStatusEnum.UNKNOWN, MqErrorEnum.MQ_EXCEPTION);
    }

    @Override
    public GenericResponseExt<TransactionStatusEnum> send(TransactionMessage message) {
        return this.send(message, null);
    }


    @Override
    public TransactionMQChecker getListener() {
        return this.transactionMqChecker;
    }

    private Object deserialize(MessageExt messageExt) {
        try {
            Deserializer deserializer = this.defaultDeserializer;
            return deserializer.deserialize(messageExt.getBody(), messageBodyClass);
        } catch (Exception ex) {
            String errorMsg = String.format("反序列化消息内容失败.[msgId:%s],[topic:%s],[tags:%s],[keys:%s]",
                    messageExt.getMsgId(),
                    messageExt.getTopic(),
                    messageExt.getTags(),
                    messageExt.getKeys()
            );
            log.error(errorMsg, ex);
            throw ex;
        }
    }


}
