package ace.fw.mq.rocketmq.autoconfigure.register.producer;

import ace.fw.mq.rocketmq.autoconfigure.property.RocketMQProducerProperty;
import ace.fw.mq.rocketmq.impl.producer.AbstractMQProducer;
import ace.fw.mq.rocketmq.impl.producer.MessageConverter;
import ace.fw.mq.rocketmq.impl.producer.RocketMQMessageChecker;
import ace.fw.mq.rocketmq.property.RocketMQProperty;
import ace.fw.mq.serializer.Serializer;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 18:30
 * @description 抽象消息生产者注册器, 根据配置动态注册多个消息生产者
 */
@Data
@Accessors(chain = true)
@Slf4j
public abstract class AbstractMQProducerRegister implements ProducerRegister {

    @Override
    public void register(RocketMQProperty rocketMQProperty, String defaultMQProducerBeanName, RocketMQProducerProperty rocketMQProducerProperty, BeanFactory beanFactory, BeanDefinitionRegistry registry) {

        String mqProducerBeanName = this.getMQProducerBeanName(defaultMQProducerBeanName, rocketMQProducerProperty);

        String rocketMQProducerBeanName = this.registerRocketMQProducer(mqProducerBeanName, rocketMQProducerProperty, beanFactory, registry);

        this.registerMqProducer(mqProducerBeanName, rocketMQProducerBeanName, rocketMQProducerProperty, beanFactory, registry);

    }

    /**
     * 注册消息生产者
     */
    protected void registerMqProducer(String mqProducerBeanName, String rocketMQProducerBeanName, RocketMQProducerProperty rocketMQProducerProperty, BeanFactory beanFactory, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(AbstractMQProducer.class, () -> {
            AbstractMQProducer mqProducer = this.createMQProducer(mqProducerBeanName, rocketMQProducerBeanName, rocketMQProducerProperty, beanFactory, registry);

            mqProducer.setDefaultSerializer(beanFactory.getBean(Serializer.class));
            mqProducer.setMqProducer(beanFactory.getBean(rocketMQProducerBeanName, MQProducer.class));
            mqProducer.setMessageConverter(beanFactory.getBean(MessageConverter.class));
            mqProducer.setRocketMQMessageChecker(beanFactory.getBean(RocketMQMessageChecker.class));

            return mqProducer;
        });

        registry.registerBeanDefinition(mqProducerBeanName, beanDefinitionBuilder.getRawBeanDefinition());
    }

    /**
     * 创建消息生产者
     *
     * @param mqProducerBeanName
     * @param rocketMQProducerBeanName
     * @param rocketMQProducerProperty
     * @param beanFactory
     * @param registry
     * @return
     */
    protected abstract AbstractMQProducer createMQProducer(String mqProducerBeanName, String rocketMQProducerBeanName, RocketMQProducerProperty rocketMQProducerProperty, BeanFactory beanFactory, BeanDefinitionRegistry registry);

    /**
     * 创建rocketmq消息生产者
     *
     * @param mqProducerBeanName
     * @param rocketMQProducerBeanName
     * @param rocketMQProducerProperty
     * @param beanFactory
     * @return
     */
    protected abstract MQProducer createRocketMQProducer(String mqProducerBeanName, String rocketMQProducerBeanName, RocketMQProducerProperty rocketMQProducerProperty, BeanFactory beanFactory);

    /**
     * 获取rocketmq消息生产者 bean name
     *
     * @param mqProducerBeanName
     * @param property
     * @return
     */
    protected abstract String getRocketMQProducerBeanName(String mqProducerBeanName, RocketMQProducerProperty property);

    /**
     * 注册rocketmq 消息生产者
     *
     * @param mqProducerBeanName 消息生产者 bean name
     * @param property           rocketmq生产者配置
     * @param beanFactory        {@link BeanFactory}
     * @param registry           注册器
     * @return rocketmq 消息生产者的bean name
     */
    protected String registerRocketMQProducer(String mqProducerBeanName, RocketMQProducerProperty property, BeanFactory beanFactory, BeanDefinitionRegistry registry) {
        String rocketMQProducerBeanName = this.getRocketMQProducerBeanName(mqProducerBeanName, property);

        MQProducer rocketMQProducer = this.createRocketMQProducer(mqProducerBeanName, rocketMQProducerBeanName, property, beanFactory);

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MQProducer.class, () -> {
            return rocketMQProducer;
        });

        registry.registerBeanDefinition(rocketMQProducerBeanName, beanDefinitionBuilder.getRawBeanDefinition());

        return rocketMQProducerBeanName;
    }

    /**
     * 获取生产者bean name
     *
     * @param defaultBeanName
     * @param property
     * @return
     */
    protected String getMQProducerBeanName(String defaultBeanName, RocketMQProducerProperty property) {
        String beanName = defaultBeanName;
        if (StringUtils.isNotBlank(property.getBeanName())) {
            beanName = property.getBeanName();
        }
        return beanName;
    }

}
