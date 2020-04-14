package ace.fw.mq.rocketmq.impl.producer;

import ace.fw.mq.model.TransactionMessage;
import lombok.Builder;
import lombok.Data;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/14 10:38
 * @description
 */
@Data
@Builder
class TransactionMessageInternalParams {
    /**
     * 业务参数
     */
    private Object bizParams;
    /**
     * 发送的消息
     */
    private TransactionMessage message;

}
