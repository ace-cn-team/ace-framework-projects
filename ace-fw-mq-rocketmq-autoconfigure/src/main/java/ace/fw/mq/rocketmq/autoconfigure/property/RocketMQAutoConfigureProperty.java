package ace.fw.mq.rocketmq.autoconfigure.property;

import ace.fw.mq.rocketmq.autoconfigure.constant.RocketMQConfigureConstants;
import ace.fw.mq.rocketmq.property.RocketMQProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 17:55
 * @description
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ConfigurationProperties(value = RocketMQConfigureConstants.CONFIG_PREFIX)
public class RocketMQAutoConfigureProperty {
    private Boolean enable = true;
    /**
     * 通用配置
     */
    private RocketMQProperty commonProperty = new RocketMQProperty();
    /**
     * 消息生产者配置
     */
    private Map<String, RocketMQProducerProperty> mqProducer = new HashMap<>();
    /**
     * 消息消费者配置
     */
    private Map<String, RocketMQConsumerProperty> mqConsumer = new HashMap<>();

}
