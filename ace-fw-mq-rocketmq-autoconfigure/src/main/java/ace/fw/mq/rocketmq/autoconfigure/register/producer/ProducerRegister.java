package ace.fw.mq.rocketmq.autoconfigure.register.producer;

import ace.fw.mq.rocketmq.autoconfigure.property.RocketMQAutoConfigureProperty;
import ace.fw.mq.rocketmq.autoconfigure.property.RocketMQProducerProperty;
import ace.fw.mq.rocketmq.property.RocketMQProperty;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/10 20:49
 * @description 消息生产者注册器
 */
public interface ProducerRegister {
    /**
     * 注册消息生产者
     */
    void register(RocketMQProperty rocketMQProperty, String defaultMQProducerBeanName, RocketMQProducerProperty rocketMQProducerProperty, BeanFactory beanFactory, BeanDefinitionRegistry registry);

    /**
     * 注册器是否支持该类型配置
     * @param rocketMQProducerProperty
     * @return
     */
    boolean isSupport(RocketMQProducerProperty rocketMQProducerProperty);
}
