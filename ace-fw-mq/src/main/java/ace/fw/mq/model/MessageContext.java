package ace.fw.mq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/3 10:51
 * @description 回调上下文
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageContext<MessageBody> {
    /**
     * {@link Topic}
     */
    private String topic;
    /**
     * topic的tags
     */
    private List<String> tags;
    /**
     * 重试回调次数
     */
    private Integer reconsumeCount;
    /**
     * 扩展参数
     */
    private Map<String, String> extProperties;
    /**
     * 消息体内容
     */
    private MessageBody messageBody;
}
