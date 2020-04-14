package ace.fw.mq.rocketmq.property;

import ace.fw.mq.enums.MQConsumerTypeEnum;
import ace.fw.mq.rocketmq.enums.MQHandlerTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 17:55
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RocketMQConsumerProperty {
    /**
     * 优先级 该属性beanName > {@link RocketMQAutoConfigureProperty#getMqConsumer()}  map的key
     */
    private String beanName = "";
    /**
     * 组名
     */
    private String groupName = "default_consumer";
    /**
     * 名称服务的地址，如：172.18.0.19:9876
     */
    private String nameServerAddress = "";
    /**
     * 一次拉取消息，拉取多少条消息,默认拉取一条
     */
    private Integer consumeMessageBatchMaxSize = 1;
    /**
     * 消费者类型 {@link MQConsumerTypeEnum}
     */
    private MQConsumerTypeEnum type = MQConsumerTypeEnum.CONSUMER;
    /**
     * 回调处理的方法 {@link MQHandlerTypeEnum},默认 {@link MQHandlerTypeEnum#CONCURRENTLY}
     */
    private MQHandlerTypeEnum mqHandlerType = MQHandlerTypeEnum.CONCURRENTLY;
}
