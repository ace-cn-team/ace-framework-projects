package ace.fw.mq.rocketmq.impl.producer;

import ace.fw.mq.model.Message;
import ace.fw.mq.model.TransactionMessage;
import ace.fw.mq.rocketmq.property.RocketMQProperty;
import ace.fw.mq.serializer.Deserializer;
import ace.fw.mq.serializer.Serializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
public class MessageConverter {

    public org.apache.rocketmq.common.message.Message toRocketMQMessage(Message message, Serializer serializer) {
        byte[] messageBodyBytes = serializer.serialize(message.getBody());

        org.apache.rocketmq.common.message.Message rocketMQMessage = new org.apache.rocketmq.common.message.Message();
        rocketMQMessage.setTopic(message.getTopic().getCode());
        rocketMQMessage.setTags(String.join(",", message.getTags()));
        rocketMQMessage.setBody(messageBodyBytes);
        return rocketMQMessage;
    }

    public org.apache.rocketmq.common.message.Message toRocketMQMessage(TransactionMessage message, Serializer serializer) {
        byte[] messageBodyBytes = serializer.serialize(message.getBody());
        org.apache.rocketmq.common.message.Message rocketMQMessage = new org.apache.rocketmq.common.message.Message();
        rocketMQMessage.setTopic(message.getTopic().getCode());
        rocketMQMessage.setTags(String.join(",", message.getTags()));
        rocketMQMessage.setBody(messageBodyBytes);
        return rocketMQMessage;
    }


    public List<String> toTagListFromTags(String tagString) {
        List<String> tags = new ArrayList<>();
        if (StringUtils.isNoneBlank(tagString)) {
            tags.addAll(Arrays.asList(tagString.split(",")));
        }
        return tags;
    }
}
