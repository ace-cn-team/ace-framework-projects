package ace.fw.mq.rocketmq.autoconfigure.register.consumer;

import ace.fw.mq.consumer.MQListener;
import ace.fw.mq.enums.MQConsumerTypeEnum;
import ace.fw.mq.rocketmq.impl.consumer.MQConsumerImpl;
import ace.fw.mq.rocketmq.property.RocketMQAutoConfigureProperty;
import ace.fw.mq.rocketmq.property.RocketMQConsumerProperty;
import ace.fw.mq.serializer.Deserializer;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

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
        ListableBeanFactory beanFactory = configurableListableBeanFactory;
        Map<String, MQListener> mqListenerMap = beanFactory.getBeansOfType(MQListener.class);
        if (MapUtils.isEmpty(mqListenerMap)) {
            log.info("can not find MQListener,exit auto configure");
            return;
        }
        mqListenerMap.entrySet()
                .stream()
                .forEach(entry -> {
                    MQListener mqListener = entry.getValue();
                    String mqListenerBeanName = entry.getKey();
                    Map.Entry<String, RocketMQConsumerProperty> rocketMQConsumerProperty = this.findRocketMQConsumerProperty(mqListenerBeanName);
                    if (Objects.isNull(rocketMQConsumerProperty)) {
                        String msg = String.format("mq listener [bean name:%s] can not find the config", mqListenerBeanName);
                        throw new RuntimeException(msg);
                    }
                    this.register(mqListener, mqListenerBeanName, rocketMQConsumerProperty, configurableListableBeanFactory);
                });
    }

    /**
     * 根据bean name 搜索相关配置,搜索到多于一个配置,则throw {@link RuntimeException}
     *
     * @param mqListenerBeanName
     * @return
     */
    private Map.Entry<String, RocketMQConsumerProperty> findRocketMQConsumerProperty(String mqListenerBeanName) {
        Predicate<Map.Entry<String, RocketMQConsumerProperty>> propertyMapKeyFilter = p -> StringUtils.equalsIgnoreCase(p.getKey(), mqListenerBeanName);
        Predicate<Map.Entry<String, RocketMQConsumerProperty>> propertyBeanNameFilter = p -> StringUtils.equalsIgnoreCase(p.getValue().getBeanName(), mqListenerBeanName);

        List<Map.Entry<String, RocketMQConsumerProperty>> properties = this.rocketMQAutoConfigureProperty
                .getMqConsumer()
                .entrySet()
                .stream()
                .filter(p -> propertyMapKeyFilter.or(propertyBeanNameFilter).test(p))
                .collect(Collectors.toList());
        if (properties.size() == 0) {
            return null;
        }
        if (properties.size() > 2) {
            String msg = String.format("[bean name:%s] mq listener config must be one,but find more than one", mqListenerBeanName);
            throw new RuntimeException(msg);
        }
        return properties.get(0);
    }

    private void register(MQListener mqListener, String mqListenerBeanName, Map.Entry<String, RocketMQConsumerProperty> entry, ConfigurableListableBeanFactory configurableListableBeanFactory) {
        RocketMQConsumerProperty rocketMQConsumerProperty = entry.getValue();
        ListableBeanFactory beanFactory = configurableListableBeanFactory;
        BeanDefinitionRegistry registry = ((BeanDefinitionRegistry) configurableListableBeanFactory);

        MQConsumerImpl mqConsumer = MQConsumerImpl.builder()
                .consumeMessageBatchMaxSize(rocketMQConsumerProperty.getConsumeMessageBatchMaxSize())
                .defaultSerializer(beanFactory.getBean(Deserializer.class))
                .groupName(rocketMQConsumerProperty.getGroupName())
                .mqHandlerType(rocketMQConsumerProperty.getMqHandlerType())
                .mqListener(mqListener)
                .nameServerAddress(rocketMQConsumerProperty.getNameServerAddress())
                .build();

        String mqConsumerBeanName = this.getMQConsumerBeanName(mqListenerBeanName);

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MQConsumerImpl.class, () -> mqConsumer);

        registry.registerBeanDefinition(mqConsumerBeanName, beanDefinitionBuilder.getRawBeanDefinition());
        String msg = String.format("mq listener [bean name:%s] register success.", mqListenerBeanName);
        log.info(msg);
    }

    private String getMQConsumerBeanName(String mqListenerBeanName) {
        return String.format("%s_MQConsumer", mqListenerBeanName);
    }

}
