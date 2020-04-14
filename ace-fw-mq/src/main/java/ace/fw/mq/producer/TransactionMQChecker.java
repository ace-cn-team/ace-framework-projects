package ace.fw.mq.producer;

import ace.fw.mq.enums.TransactionStatusEnum;
import ace.fw.mq.model.MessageContext;
import ace.fw.mq.model.Topic;
import ace.fw.mq.model.TransactionMessage;
import ace.fw.mq.serializer.Deserializer;
import org.apache.commons.collections.ListUtils;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/2 1:06
 * @description 事务MQ回调本地事务方法与回查本地事务结果方法接口
 */
public interface TransactionMQChecker<MessageBody> {
    /**
     * When send transactional prepare(half) message succeed, this method will be invoked to execute local transaction.
     *
     * @param message Half(prepare) message
     * @param arg     Custom business parameter
     * @return Transaction state
     */
    TransactionStatusEnum executeLocalTransaction(final TransactionMessage<MessageBody> message, final Object arg);

    /**
     * When no response to prepare(half) message. broker will send check message to check the transaction status, and this
     * method will be invoked to get local transaction status.
     *
     * @param messageContext Check message context
     * @return Transaction state
     */
    TransactionStatusEnum checkLocalTransaction(final MessageContext<MessageBody> messageContext);
}
