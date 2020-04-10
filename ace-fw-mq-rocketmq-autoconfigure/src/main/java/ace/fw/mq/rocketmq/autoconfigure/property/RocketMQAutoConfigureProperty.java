package ace.fw.mq.rocketmq.autoconfigure.property;

import ace.fw.mq.rocketmq.property.RocketMQProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 17:55
 * @description
 */
@Data
@ConfigurationProperties(value = "ace.rocketmq")
public class RocketMQAutoConfigureProperty {
    private Boolean enable = true;

    private RocketMQProperty commonProperty;
    /**
     * 普通消息生产者配置
     */
    private Map<String, RocketMQProducerProperty> mqProducer;
}
