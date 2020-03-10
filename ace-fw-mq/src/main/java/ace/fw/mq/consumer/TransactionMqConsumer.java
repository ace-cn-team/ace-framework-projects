package ace.fw.mq.consumer;

import ace.fw.model.response.GenericResponseExt;
import ace.fw.mq.enums.TransactionStatusEnum;
import ace.fw.mq.model.TransactionMessage;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/2 1:06
 * @description 事务MQ Producer
 */
public interface TransactionMqConsumer<T> {
    /**
     * 同步发送普通MQ
     *
     * @param transactionMessage
     * @return data字段 操作结果
     */
    GenericResponseExt<TransactionStatusEnum> send(TransactionMessage transactionMessage);
}
