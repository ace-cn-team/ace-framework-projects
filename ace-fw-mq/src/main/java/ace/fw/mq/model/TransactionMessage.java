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
public class TransactionMessage<MessageBody> {
    /**
     * 消息体内容
     */
    private MessageBody body;
    /**
     * 主题
     */
    private Topic topic;
    /**
     * tags
     */
    private List<String> tags;
}
