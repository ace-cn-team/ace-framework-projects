package ace.fw.mq.rocketmq.property;

import ace.fw.mq.rocketmq.constants.RocketMQConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 9:44
 * @description
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RocketMQProperty {
    /**
     * 默认消息体大小 2M
     */
    private Integer messageBodyMaxBytes = RocketMQConstant.DEFAULT_MESSAGE_BODY_MAX_BYTES;
    /**
     * 消息体超过默认大小（1M)，记录警告日志
     */
    private Integer warningMessageBodyMaxBytes = RocketMQConstant.DEFAULT_WARNING_MESSAGE_BODY_MAX_BYTES;
}
