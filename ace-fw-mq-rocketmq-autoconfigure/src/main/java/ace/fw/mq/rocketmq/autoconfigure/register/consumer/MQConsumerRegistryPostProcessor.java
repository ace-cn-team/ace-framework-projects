package ace.fw.mq.rocketmq.autoconfigure.register.consumer;

import ace.fw.mq.consumer.MQListener;
import ace.fw.mq.enums.MQConsumerTypeEnum;
import ace.fw.mq.enums.MQProducerTypeEnum;
import ace.fw.mq.rocketmq.impl.consumer.MQConsumerImpl;
import ace.fw.mq.rocketmq.property.RocketMQAutoConfigureProperty;
import ace.fw.mq.rocketmq.property.RocketMQConsumerProperty;
import ace.fw.mq.serializer.Deserializer;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 18:30
 * @description 消息消费者注册器, 根据配置动态注册多个消息消费者
 */
@Data
@Builder
@Slf4j
public class MQConsumerRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private RocketMQAutoConfigureProperty rocketMQAutoConfigureProperty;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        if (MapUtils.isEmpty(rocketMQAutoConfigureProperty.getMqConsumer())) {
            log.info("can not find mq consumer config,exit auto configure");
            return;
        }
        ListableBeanFactory beanFactory = configurableListableBeanFactory;
        rocketMQAutoConfigureProperty
                .getMqConsumer()
                .entrySet()
                .stream()
                .forEach(entry -> {
                    String defaultMQListenerBeanName = entry.getKey();
                    RocketMQConsumerProperty rocketMQConsumerProperty = entry.getValue();
                    String mqListenerBeanName = StringUtils.isNoneBlank(entry.getValue().getBeanName()) ? entry.getValue().getBeanName() : defaultMQListenerBeanName;

                    this.register(mqListenerBeanName, rocketMQConsumerProperty, configurableListableBeanFactory);
                });
    }


    private void register(String mqListenerBeanName, RocketMQConsumerProperty rocketMQConsumerProperty, ConfigurableListableBeanFactory configurableListableBeanFactory) {
        BeanDefinitionRegistry registry = ((BeanDefinitionRegistry) configurableListableBeanFactory);

        MQConsumerImpl mqConsumer = MQConsumerImpl.builder()
                .consumeMessageBatchMaxSize(rocketMQConsumerProperty.getConsumeMessageBatchMaxSize())
                .groupName(rocketMQConsumerProperty.getGroupName())
                .mqHandlerType(rocketMQConsumerProperty.getMqHandlerType())
                .nameServerAddress(rocketMQConsumerProperty.getNameServerAddress())
                .build();

        String mqConsumerBeanName = this.getMQConsumerBeanName(mqListenerBeanName);

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MQConsumerImpl.class, () -> mqConsumer)
                .addAutowiredProperty("defaultSerializer")
                .addPropertyReference("mqListener", mqListenerBeanName);

        registry.registerBeanDefinition(mqConsumerBeanName, beanDefinitionBuilder.getRawBeanDefinition());
        String msg = String.format("mq listener [bean name:%s] register success.", mqListenerBeanName);
        log.info(msg);
    }

    private String getMQConsumerBeanName(String mqListenerBeanName) {
        return String.format("%s_MQConsumer", mqListenerBeanName);
    }

}
