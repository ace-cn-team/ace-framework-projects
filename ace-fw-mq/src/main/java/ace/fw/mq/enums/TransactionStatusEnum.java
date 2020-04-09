package ace.fw.mq.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/2 19:37
 * @description
 */
public enum TransactionStatusEnum {
    /**
     * 提交事务，对应的事务消息将投递给消费者
     */
    COMMIT_TRANSACTION,
    /**
     * 回滚事务，对应的事务消息会被删除，不会投递给消费者
     */
    ROLLBACK_TRANSACTION,
    /**
     * 未知状态，一般在用户无法确定事务是成功还是失败时使用，对于未知状态的事务，服务端会定期进行事务回查
     */
    UNKNOWN;

}