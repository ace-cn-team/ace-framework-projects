package ace.fw.mq.rocketmq.impl;

import ace.fw.mq.model.Message;
import ace.fw.mq.model.TransactionMessage;
import ace.fw.mq.rocketmq.property.RocketMQProperty;
import ace.fw.mq.serializer.Serializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 11:28
 * @description
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@Builder
@Slf4j
class MessageConverter {
    private RocketMQProperty rocketMQProperty;

    public org.apache.rocketmq.common.message.Message toRocketMQMessage(Message message, Serializer serializer) {
        byte[] messageBodyBytes = serializer.serialize(message.getBody());
        org.apache.rocketmq.common.message.Message rocketMQMessage = new org.apache.rocketmq.common.message.Message();
        rocketMQMessage.setTopic(message.getTopic().getCode());
        rocketMQMessage.setKeys(message.getTags());
        rocketMQMessage.setBody(messageBodyBytes);
        return rocketMQMessage;
    }

    public org.apache.rocketmq.common.message.Message toRocketMQMessage(TransactionMessage transactionMessage, Serializer serializer) {
        byte[] messageBodyBytes = serializer.serialize(transactionMessage.getBody());
        org.apache.rocketmq.common.message.Message rocketMQMessage = new org.apache.rocketmq.common.message.Message();
        rocketMQMessage.setTopic(transactionMessage.getTopic().getCode());
        rocketMQMessage.setKeys(transactionMessage.getTags());
        rocketMQMessage.setBody(messageBodyBytes);
        return rocketMQMessage;
    }
}
