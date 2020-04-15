package ace.fw.mq.producer;

import ace.fw.model.response.GenericResponseExt;
import ace.fw.mq.enums.TransactionStatusEnum;
import ace.fw.mq.model.TransactionMessage;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/2 1:06
 * @description 事务MQ Producer,发送半事务MQ之后回调本地事务方法与回查本地事务结果方法，必须实现{@link TransactionMQChecker}接口
 */
public interface TransactionMQProducer<MessageBody, LogicParams> {
    /**
     * 同步发送事务MQ
     *
     * @param message 事务消息
     * @param args    本地事务业务参数
     * @return code字段 0代表事务半消息发送成功，0以外代码代表事务半消息发送失败
     * data字段 - code字段为0时候代表事务半消息发送成功时候的事务状态
     * data字段 - code字段为0以外时候，永远为{@link TransactionStatusEnum#UNKNOWN}
     */
    GenericResponseExt<TransactionStatusEnum> send(final TransactionMessage<MessageBody> message, final LogicParams args);

    /**
     * 同步发送事务MQ
     *
     * @param message 事务消息
     * @return code字段 0代表事务半消息发送成功，0以外代码代表事务半消息发送失败
     * data字段 - code字段为0时候代表事务半消息发送成功时候的事务状态
     * data字段 - code字段为0以外时候，永远为{@link TransactionStatusEnum#UNKNOWN}
     */
    GenericResponseExt<TransactionStatusEnum> send(final TransactionMessage<MessageBody> message);

//    /**
//     * 返回绑定的事务MQ处理器
//     *
//     * @return
//     */
//    TransactionMQChecker<MessageBody, LogicParams> getListener();
}
