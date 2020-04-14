package ace.fw.mq.rocketmq.autoconfigure.register.producer;

import ace.fw.mq.enums.MQProducerTypeEnum;
import ace.fw.mq.rocketmq.impl.producer.MQProducerImpl;
import ace.fw.mq.rocketmq.impl.producer.MessageConverter;
import ace.fw.mq.rocketmq.impl.producer.RocketMQMessageChecker;
import ace.fw.mq.rocketmq.property.RocketMQAutoConfigureProperty;
import ace.fw.mq.rocketmq.property.RocketMQProducerProperty;
import ace.fw.mq.serializer.Serializer;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanNameReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.List;
import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 18:30
 * @description 普通消息生产者注册器, 根据配置动态注册多个普通消息生产者
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
            log.info("can not find mq producer config,exit auto configure");
            return;
        }
        rocketMQAutoConfigureProperty
                .getMqProducer()
                .entrySet()
                .stream()
                .filter(p -> MQProducerTypeEnum.PRODUCER.equals(p.getValue().getType()))
                .forEach(entry -> {
                    this.register(rocketMQAutoConfigureProperty, entry, beanFactory);
                });

    }

    private void register(RocketMQAutoConfigureProperty rocketMQAutoConfigureProperty, Map.Entry<String, RocketMQProducerProperty> entry, ConfigurableListableBeanFactory configurableListableBeanFactory) {

        String defaultMQProducerBeanName = entry.getKey();
        RocketMQProducerProperty rocketMQProducerProperty = entry.getValue();
        ListableBeanFactory beanFactory = configurableListableBeanFactory;
        BeanDefinitionRegistry registry = ((BeanDefinitionRegistry) configurableListableBeanFactory);

        String mqProducerBeanName = this.getMQProducerBeanName(defaultMQProducerBeanName, rocketMQProducerProperty);

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MQProducerImpl.class, () -> {
            MQProducerImpl mqProducer = MQProducerImpl
                    .builder()
                    .groupName(rocketMQProducerProperty.getGroupName())
                    .nameServerAddress(rocketMQProducerProperty.getNameServerAddress())
                    .build();

            return mqProducer;
        })
                .addAutowiredProperty("defaultSerializer")
                .addAutowiredProperty("messageConverter")
                .addAutowiredProperty("rocketMQMessageChecker");

        registry.registerBeanDefinition(mqProducerBeanName, beanDefinitionBuilder.getRawBeanDefinition());

        log.info(String.format("mq producer [bean:%s] register success", mqProducerBeanName));
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
