package ace.fw.mq.rocketmq.impl.producer;

import ace.fw.mq.enums.TransactionStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 16:52
 * @description
 */

@Slf4j
public final class TransactionStatusConverter {

    static {
        Map<LocalTransactionState, TransactionStatusEnum> tmp = new HashMap<>();
        tmp.put(LocalTransactionState.COMMIT_MESSAGE, TransactionStatusEnum.COMMIT_TRANSACTION);
        tmp.put(LocalTransactionState.ROLLBACK_MESSAGE, TransactionStatusEnum.ROLLBACK_TRANSACTION);
        tmp.put(LocalTransactionState.UNKNOW, TransactionStatusEnum.UNKNOWN);
        relationMap = tmp;
    }

    private static Map<LocalTransactionState, TransactionStatusEnum> relationMap;

    public static LocalTransactionState toRocketMQTransactionState(TransactionStatusEnum transactionStatusEnum) {
        LocalTransactionState result = relationMap.entrySet().stream()
                .filter(p -> p.getValue().equals(transactionStatusEnum))
                .map(p -> p.getKey())
                .findFirst()
                .orElse(null);
        if (Objects.isNull(result)) {
            throw new RuntimeException("没有实现对应枚举问题");
        }
        return result;

    }

    public static TransactionStatusEnum toTransactionStatus(LocalTransactionState localTransactionState) {
        TransactionStatusEnum result = relationMap.entrySet().stream()
                .filter(p -> p.getKey().equals(localTransactionState))
                .map(p -> p.getValue())
                .findFirst()
                .orElse(null);
        if (Objects.isNull(result)) {
            throw new RuntimeException("没有实现对应枚举问题");
        }
        return result;
    }
}
