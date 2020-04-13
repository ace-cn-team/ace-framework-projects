package ace.fw.mq.rocketmq.autoconfigure.register.consumer;

import ace.fw.mq.consumer.MQListener;
import ace.fw.mq.rocketmq.autoconfigure.property.RocketMQConsumerProperty;
import ace.fw.mq.rocketmq.autoconfigure.property.RocketMQProducerProperty;
import ace.fw.mq.rocketmq.property.RocketMQProperty;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/10 20:49
 * @description 消息消费者注册器
 */
public interface ConsumerRegister {
    /**
     * 注册消息消费者
     */
    void register(MQListener mqListener, String mqListenerBeanName, RocketMQConsumerProperty rocketMQConsumerProperty, ListableBeanFactory beanFactory, BeanDefinitionRegistry registry);

    /**
     * 注册器是否支持该类型配置
     *
     * @param rocketMQConsumerProperty
     * @return
     */
    boolean isSupport(RocketMQConsumerProperty rocketMQConsumerProperty);
}
