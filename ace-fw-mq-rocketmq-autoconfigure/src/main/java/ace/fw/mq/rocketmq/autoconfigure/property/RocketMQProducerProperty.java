package ace.fw.mq.rocketmq.autoconfigure.property;

import ace.fw.mq.enums.MQProducerTypeEnum;
import lombok.Data;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 17:55
 * @description
 */
@Data
public class RocketMQProducerProperty {
    /**
     * 优先级 该属性beanName > {@link RocketMQAutoConfigureProperty#getMqProducer()} map的key
     */
    private String beanName = "";
    /**
     * 组名
     */
    private String groupName;
    /**
     * 名称服务的地址，如：172.18.0.19:9876
     */
    private String nameServerAddress;
    /**
     * 生产者类型 {@link MQProducerTypeEnum}
     */
    private MQProducerTypeEnum type;
}
