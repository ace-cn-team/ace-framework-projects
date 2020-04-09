package ace.fw.mq.rocketmq.impl;

import ace.fw.model.response.GenericResponseExt;
import ace.fw.mq.enums.MqErrorEnum;
import ace.fw.mq.model.Message;
import ace.fw.mq.producer.MQProducer;
import ace.fw.mq.serializer.Serializer;
import ace.fw.util.GenericResponseExtUtils;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/8 17:56
 * @description
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
//@NoArgsConstructor
@Builder
@Slf4j
public class DefaultMQProducerImpl
        extends AbstractMQProducer
        implements MQProducer {

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
            SendResult sendResult = this.getMqProducer().send(rocketMQMessage);
            if (SendStatus.SEND_OK.equals(sendResult.getSendStatus()) == false) {
                return GenericResponseExtUtils.buildWithDataAndCodeEnum(false, MqErrorEnum.MQ_EXCEPTION);
            }
            return GenericResponseExtUtils.buildSuccessWithData(true);
        } catch (Exception ex) {
            List<String> tags = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(message.getTags())) {
                tags.addAll(message.getTags());
            }
            String msg = String.format("[消息发送失败],[topic:%s-%s-%s]",
                    message.getTopic().getCode(),
                    message.getTopic().getDesc(),
                    String.join(",", tags));
            log.error(msg, ex);
        }
        return GenericResponseExtUtils.buildWithDataAndCodeEnum(false, MqErrorEnum.MQ_EXCEPTION);
    }
}
