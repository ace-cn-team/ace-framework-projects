package ace.fw.mq.model;

import ace.fw.mq.enums.TransactionStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/2 10:45
 * @description
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionMessage {
    /**
     * 消息体内容
     */
    private Object body;
    /**
     * 主题
     */
    private Topic topic;
    /**
     * tags
     */
    private List<String> tags;
    /**
     * 事务消息半发送成功后，执行的事件
     */
    private Supplier<TransactionStatusEnum> transactionEvent;
}
