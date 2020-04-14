package ace.fw.mq.rocketmq.impl.producer;

import ace.fw.model.response.GenericResponseExt;
import ace.fw.mq.enums.MqErrorEnum;
import ace.fw.mq.model.Message;
import ace.fw.mq.producer.MQProducer;
import ace.fw.mq.serializer.Serializer;
import ace.fw.util.GenericResponseExtUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/8 17:56
 * @description
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public class MQProducerImpl
        implements MQProducer, InitializingBean, DisposableBean {
    /**
     * 底层事务MQ生产者实现
     */

    private org.apache.rocketmq.client.producer.MQProducer rocketMQProducer;
    /**
     * 序列化工具
     */

    private Serializer defaultSerializer;
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


    @Override
    public void destroy() {
        if (Objects.nonNull(this.rocketMQProducer)) {
            this.rocketMQProducer.shutdown();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Assert.notNull(this.defaultSerializer, "defaultSerializer can not be null");
        Assert.notNull(this.rocketMQMessageChecker, "rocketMQMessageChecker can not be null");
        Assert.notNull(this.messageConverter, "messageConverter can not be null");
        Assert.notNull(this.nameServerAddress, "nameServerAddress can not be null");
        Assert.notNull(this.groupName, "groupName can not be null");
        this.rocketMQProducer = this.createRocketMQProducer();

        this.rocketMQProducer.start();
    }

    private org.apache.rocketmq.client.producer.MQProducer createRocketMQProducer() {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(groupName);
        defaultMQProducer.setNamesrvAddr(this.nameServerAddress);

        return defaultMQProducer;
    }

    @Override
    public GenericResponseExt<Boolean> send(Message message) {
        return this.send(message, this.getDefaultSerializer());
    }

    @Override
    public GenericResponseExt<Boolean> send(Message message, Serializer serializer) {

        val rocketMQMessage = this.getMessageConverter().toRocketMQMessage(message, serializer);

        GenericResponseExt<Boolean> checkRocketMQMessageResult = this.getRocketMQMessageChecker().check(rocketMQMessage);
        if (checkRocketMQMessageResult.getSuccess() == false) {
            return checkRocketMQMessageResult;
        }

        try {
            SendResult sendResult = this.getRocketMQProducer().send(rocketMQMessage);
            if (SendStatus.SEND_OK.equals(sendResult.getSendStatus()) == false) {
                return GenericResponseExtUtils.buildWithDataAndCodeEnum(false, MqErrorEnum.MQ_EXCEPTION);
            }
            return GenericResponseExtUtils.buildSuccessWithData(true);
        } catch (Exception ex) {
            List<String> tags = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(message.getTags())) {
                tags.addAll(message.getTags());
            }
            String msg = String.format("[消息发送失败],[topic:%s][topic-desc:%s][tags:%s]",
                    message.getTopic().getCode(),
                    message.getTopic().getDesc(),
                    String.join(",", tags));
            log.error(msg, ex);
        }
        return GenericResponseExtUtils.buildWithDataAndCodeEnum(false, MqErrorEnum.MQ_EXCEPTION);
    }
}
