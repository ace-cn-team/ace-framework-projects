package ace.fw.mq.rocketmq.autoconfigure.register.producer;

import ace.fw.mq.enums.MQProducerTypeEnum;
import ace.fw.mq.rocketmq.autoconfigure.property.RocketMQProducerProperty;
import ace.fw.mq.rocketmq.impl.producer.AbstractMQProducer;
import ace.fw.mq.rocketmq.impl.producer.MQProducerImpl;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.Assert;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 18:30
 * @description 普通消息生产者注册器, 根据配置动态注册多个消息生产者
 */
@Data
@Accessors(chain = true)
@Builder
//@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class MQProducerRegister extends AbstractMQProducerRegister {


    @Override
    protected AbstractMQProducer createMQProducer(String mqProducerBeanName, String rocketMQProducerBeanName, RocketMQProducerProperty rocketMQProducerProperty, BeanFactory beanFactory, BeanDefinitionRegistry registry) {
        return new MQProducerImpl();
    }

    @Override
    protected MQProducer createRocketMQProducer(String mqProducerBeanName, String rocketMQProducerBeanName, RocketMQProducerProperty rocketMQProducerProperty, BeanFactory beanFactory) {
        Assert.state(StringUtils.isNoneBlank(rocketMQProducerProperty.getGroupName()), "groupName can not be empty");
        Assert.state(StringUtils.isNoneBlank(rocketMQProducerProperty.getNameServerAddress()), "nameServerAddress can not be empty");
        DefaultMQProducer mqProducer = new DefaultMQProducer(rocketMQProducerProperty.getGroupName());
        mqProducer.setNamesrvAddr(rocketMQProducerProperty.getNameServerAddress());
        return mqProducer;
    }

    @Override
    protected String getRocketMQProducerBeanName(String mqProducerBeanName, RocketMQProducerProperty property) {
        String rocketMQProducerBeanName = mqProducerBeanName + "_RocketMQProducer";

        return rocketMQProducerBeanName;
    }

    @Override
    public boolean isSupport(RocketMQProducerProperty rocketMQProducerProperty) {
        return MQProducerTypeEnum.PRODUCER.equals(rocketMQProducerProperty.getType());
    }
}
