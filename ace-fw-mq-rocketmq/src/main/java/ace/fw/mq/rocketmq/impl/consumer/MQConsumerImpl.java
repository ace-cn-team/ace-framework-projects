package ace.fw.mq.rocketmq.impl.consumer;

import ace.fw.mq.consumer.MQListener;
import ace.fw.mq.enums.MQResponseEnum;
import ace.fw.mq.model.MessageContext;
import ace.fw.mq.rocketmq.enums.MQHandlerTypeEnum;
import ace.fw.mq.serializer.Deserializer;
import ace.fw.util.GenericClassUtils;
import ace.fw.util.ReflectionUtils;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/11 10:24
 * @description
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class MQConsumerImpl implements InitializingBean, DisposableBean {
    /**
     * 消息消费监听者
     */

    private MQListener mqListener;

    /**
     * 反序列化工具
     */

    private Deserializer defaultSerializer;
    /**
     * 消息消费者执行方式
     */
    private MQHandlerTypeEnum mqHandlerType;
    /**
     * rocketmq名称服务地址
     */
    private String nameServerAddress;
    /**
     * rocketmq 消费者组名
     */
    private String groupName;
    /**
     * 默认一次拉取消息，只拉取一条消息
     */
    private Integer consumeMessageBatchMaxSize = 1;
    /**
     * 消息消费拉取器
     */
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private MQPushConsumer mqPushConsumer;
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Class messageBodyClass;


    @Override
    public void destroy() throws Exception {
        if (Objects.nonNull(this.mqPushConsumer)) {
            this.mqPushConsumer.shutdown();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Assert.notNull(this.defaultSerializer, "defaultSerializer can not be null");
        Assert.notNull(this.mqHandlerType, "mqHandlerType can not be null");
        Assert.notNull(this.mqListener, "mqListener can not be null");
        messageBodyClass = ReflectionUtils.getInterfaceGeneric(this.mqListener.getClass(), 0);
        this.mqPushConsumer = this.createDefaultMQPushConsumer();
    }

    private DefaultMQPushConsumer createDefaultMQPushConsumer() throws Exception {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(this.groupName);
        defaultMQPushConsumer.setNamesrvAddr(this.nameServerAddress);
        if (MQHandlerTypeEnum.CONCURRENTLY.equals(mqHandlerType)) {
            defaultMQPushConsumer.registerMessageListener(this.registerMessageListenerConcurrently());
        } else if (MQHandlerTypeEnum.ORDERLY.equals(mqHandlerType)) {
            defaultMQPushConsumer.registerMessageListener(this.registerMessageListenerOrderly());
        } else {
            throw new RuntimeException("无法识别类型," + mqHandlerType.getDesc());
        }
        String topic = this.mqListener.getSubscribeTopic().getCode();
        String tags = "*";
        if (CollectionUtils.isNotEmpty(this.mqListener.getSubscribeTags())) {
            tags = StringUtils.join(this.mqListener.getSubscribeTags(), ",");
        }

        defaultMQPushConsumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        defaultMQPushConsumer.subscribe(topic, tags);
        defaultMQPushConsumer.start();
        return defaultMQPushConsumer;
    }

    private MessageListenerOrderly registerMessageListenerOrderly() {
        return (msgs, context) -> MQConsumerImpl.this.consumeMessage(msgs, context);
    }

    private MessageListenerConcurrently registerMessageListenerConcurrently() {
        return (msgs, context) -> MQConsumerImpl.this.consumeMessage(msgs, context);
    }

    private ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {

        for (MessageExt messageExt : msgs) {
            try {
                MessageContext messageContext = this.createMessageContext(messageExt);
                MQResponseEnum mqResponseEnum = this.mqListener.consume(messageContext);
                if (mqResponseEnum.equals(MQResponseEnum.ReconsumeLater)) {
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            } catch (Exception ex) {
                String msg = String.format("mq consume failure.[topic:%s,tags:%s,msgId:%s]", messageExt.getTopic(), messageExt.getTags(), messageExt.getMsgId());
                log.error(msg, ex);
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }

    private ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt messageExt : msgs) {
            try {
                MessageContext messageContext = this.createMessageContext(messageExt);
                MQResponseEnum mqResponseEnum = this.mqListener.consume(messageContext);
                if (mqResponseEnum.equals(MQResponseEnum.ReconsumeLater)) {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            } catch (Exception ex) {
                String msg = String.format("mq consume failure.[topic:%s,tags:%s,msgId:%s]", messageExt.getTopic(), messageExt.getTags(), messageExt.getMsgId());
                log.error(msg, ex);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }


    private MessageContext createMessageContext(MessageExt messageExt) {
        List<String> tags = this.getTagsFromMessageExt(messageExt);

        Object messageBody = this.deserialize(messageExt);

        MessageContext messageContext = MessageContext.builder()
                .reconsumeCount(messageExt.getReconsumeTimes())
                .extProperties(messageExt.getProperties())
                .topic(messageExt.getTopic())
                .tags(tags)
                .messageBody(messageBody)
                .build();

        return messageContext;
    }

    private List<String> getTagsFromMessageExt(MessageExt messageExt) {
        List<String> tags = new ArrayList<>();
        if (StringUtils.isNoneBlank(messageExt.getTags())) {
            tags.addAll(Arrays.asList(messageExt.getTags().split(",")));
        }
        return tags;
    }

    private Object deserialize(MessageExt messageExt) {
        Deserializer deserializer = this.mqListener.getDeserializer();
        if (Objects.isNull(deserializer)) {
            deserializer = this.defaultSerializer;
        }
        return deserializer.deserialize(messageExt.getBody(), messageBodyClass);
    }

}
