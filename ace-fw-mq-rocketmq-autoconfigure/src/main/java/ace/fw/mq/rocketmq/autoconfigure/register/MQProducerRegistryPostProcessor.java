package ace.fw.mq.rocketmq.autoconfigure.register;

import ace.fw.mq.enums.MQProducerTypeEnum;
import ace.fw.mq.rocketmq.autoconfigure.property.RocketMQAutoConfigureProperty;
import ace.fw.mq.rocketmq.autoconfigure.property.RocketMQProducerProperty;
import ace.fw.mq.rocketmq.impl.AbstractMQProducer;
import ace.fw.mq.rocketmq.impl.MQProducerImpl;
import ace.fw.mq.rocketmq.impl.TransactionMQProducerImpl;
import ace.fw.mq.serializer.Serializer;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 18:30
 * @description 普通消息生产者注册器, 根据配置动态注册多个消息生产者
 */
@Data
@Builder
@Slf4j
public class MQProducerRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private RocketMQAutoConfigureProperty rocketMQAutoConfigureProperty;


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        if (MapUtils.isEmpty(rocketMQAutoConfigureProperty.getMqProducer())) {
            return;
        }
        rocketMQAutoConfigureProperty
                .getMqProducer()
                .entrySet()
                .stream()
                .forEach(entry -> {
                    this.registerMQProducers(entry, beanFactory);
                });

    }

    /**
     * 动态注册多个消息生产者
     *
     * @param entry
     * @param beanFactory
     */
    private void registerMQProducers(Map.Entry<String, RocketMQProducerProperty> entry, ConfigurableListableBeanFactory beanFactory) {
        DefaultListableBeanFactory registry = ((DefaultListableBeanFactory) beanFactory);
        RocketMQProducerProperty property = entry.getValue();
        String mqProducerBeanName = this.getBeanName(entry.getKey(), property);

        this.registerMQProducer(mqProducerBeanName, property, beanFactory, registry);

    }

    /**
     * 注册消息生产者
     *
     * @param mqProducerBeanName 消息生产者 bean name
     * @param property           消息生产者配置
     * @param registry           注册器
     */
    private void registerMQProducer(String mqProducerBeanName, RocketMQProducerProperty property, ConfigurableListableBeanFactory beanFactory, DefaultListableBeanFactory registry) {

        String rocketMQProducerBeanName = this.registerRocketMQProducer(mqProducerBeanName, property, registry);

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(AbstractMQProducer.class, () -> {
            AbstractMQProducer mqProducer = this.createMQProducer(property);

            mqProducer.setDefaultSerializer(beanFactory.getBean(Serializer.class));
            mqProducer.setMqProducer(beanFactory.getBean(rocketMQProducerBeanName, MQProducer.class));
            mqProducer.setRocketMQProperty(rocketMQAutoConfigureProperty.getCommonProperty());
            return mqProducer;
        });

        registry.registerBeanDefinition(mqProducerBeanName, beanDefinitionBuilder.getRawBeanDefinition());
    }

    /**
     * 注册rocketmq 消息生产者
     *
     * @param mqProducerBeanName 消息生产者 bean name
     * @param property           rocketmq生产者配置
     * @param registry           注册器
     * @return rocketmq 消息生产者的bean name
     */
    private String registerRocketMQProducer(String mqProducerBeanName, RocketMQProducerProperty property, DefaultListableBeanFactory registry) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MQProducer.class, () -> {
            MQProducer rocketMQProducer = this.createRocketMQProducer(property);

            return rocketMQProducer;
        });
        String rocketMQProducerBeanName = mqProducerBeanName + "_RocketMQProducer";
        registry.registerBeanDefinition(rocketMQProducerBeanName, beanDefinitionBuilder.getRawBeanDefinition());
        return rocketMQProducerBeanName;
    }

    private AbstractMQProducer createMQProducer(RocketMQProducerProperty property) {
        if (MQProducerTypeEnum.PRODUCER.equals(property.getType())) {
            return new MQProducerImpl();
        }
        if (MQProducerTypeEnum.TRANSACTION_PRODUCER.equals(property.getType())) {
            return new TransactionMQProducerImpl();
        }
        throw new RuntimeException("没有该类型的实现," + property.getType().getDesc());
    }

    private MQProducer createRocketMQProducer(RocketMQProducerProperty property) {
        if (MQProducerTypeEnum.PRODUCER.equals(property.getType())) {
            return this.createRocketMQNormalProducer(property);
        }
        if (MQProducerTypeEnum.TRANSACTION_PRODUCER.equals(property.getType())) {
            return this.createTransactionMQProducer(property);
        }
        throw new RuntimeException("没有该类型的实现," + property.getType().getDesc());
    }

    private MQProducer createTransactionMQProducer(RocketMQProducerProperty property) {
        Assert.state(StringUtils.isNoneBlank(property.getGroupName()), "groupName can not be empty");
        Assert.state(StringUtils.isNoneBlank(property.getNameServerAddress()), "nameServerAddress can not be empty");
        TransactionMQProducer mqProducer = new TransactionMQProducer(property.getGroupName());

        return mqProducer;
    }

    private MQProducer createRocketMQNormalProducer(RocketMQProducerProperty property) {
        Assert.state(StringUtils.isNoneBlank(property.getGroupName()), "groupName can not be empty");
        Assert.state(StringUtils.isNoneBlank(property.getNameServerAddress()), "nameServerAddress can not be empty");
        DefaultMQProducer mqProducer = new DefaultMQProducer(property.getGroupName());
        mqProducer.setNamesrvAddr(property.getNameServerAddress());
        return mqProducer;
    }


    private String getBeanName(String defaultBeanName, RocketMQProducerProperty property) {
        String beanName = defaultBeanName;
        if (StringUtils.isNotBlank(property.getBeanName())) {
            beanName = property.getBeanName();
        }
        return beanName;
    }

    private Class<?> getMQProducerBeanClassByType(MQProducerTypeEnum type) {
        switch (type) {
            case PRODUCER:
                return MQProducerImpl.class;
            case TRANSACTION_PRODUCER:
                return TransactionMQProducerImpl.class;
            default:
                throw new RuntimeException("没有该类型实现," + type.getDesc());
        }
    }

}
