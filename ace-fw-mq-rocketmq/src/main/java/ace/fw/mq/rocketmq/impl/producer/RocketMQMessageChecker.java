package ace.fw.mq.rocketmq.impl.producer;

import ace.fw.model.response.GenericResponseExt;
import ace.fw.mq.enums.MqErrorEnum;
import ace.fw.mq.rocketmq.constants.RocketMQConstant;
import ace.fw.mq.rocketmq.property.RocketMQProperty;
import ace.fw.util.GenericResponseExtUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 11:18
 * @description 检查rocketmq 消息的限制
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@Builder
@Slf4j
public class RocketMQMessageChecker {
    private RocketMQProperty rocketMQProperty;

    public GenericResponseExt<Boolean> check(org.apache.rocketmq.common.message.Message message) {
        int currentLen = message.getBody().length;

        //消息体大小超过用户设置的大小，记录日志，并返回失败
        if (currentLen > rocketMQProperty.getMessageBodyMaxBytes()) {
            Object[] params = {currentLen, rocketMQProperty.getMessageBodyMaxBytes(), message.getTopic(), message.getTags(), message.getKeys()};
            log.error("Mq send Failure, Because Message size is {} greater than {}, topic:[{}], tag:[{}], key:[{}]", params);
            return GenericResponseExtUtils.buildWithDataAndCodeEnum(false, MqErrorEnum.CUSTOM_MESSAGE_BODY_LIMIT_ERROR);
        }

        //消息体大小超过MQ服务器设置的大小，记录日志，并返回失败
        if (currentLen > RocketMQConstant.MESSAGE_BODY_MAX_BYTES) {
            Object[] params = {currentLen, RocketMQConstant.MESSAGE_BODY_MAX_BYTES, message.getTopic(), message.getTags(), message.getKeys()};
            log.error("Mq send Failure, Because Message size is {} greater than {}, topic:[{}], tag:[{}], key:[{}]", params);
            return GenericResponseExtUtils.buildWithDataAndCodeEnum(false, MqErrorEnum.MESSAGE_BODY_LIMIT_ERROR);
        }

        //消息体大小超过自定义警告大小（默认1M)，记录日志
        if (currentLen > rocketMQProperty.getWarningMessageBodyMaxBytes()) {
            Object[] params = {currentLen, message.getTopic(), message.getTags(), message.getKeys()};
            log.warn("Mq send big Message size is {}, topic:[{}], tag:[{}], key:[{}]", params);
        }
        return GenericResponseExtUtils.buildSuccessWithData(true);
    }
}
