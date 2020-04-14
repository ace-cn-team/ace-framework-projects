package ace.fw.mq.rocketmq.autoconfigure.register.producer;

import ace.fw.mq.enums.MQProducerTypeEnum;
import ace.fw.mq.producer.TransactionMQListener;
import ace.fw.mq.rocketmq.impl.producer.MessageConverter;
import ace.fw.mq.rocketmq.impl.producer.RocketMQMessageChecker;
import ace.fw.mq.rocketmq.impl.producer.TransactionMQProducerImpl;
import ace.fw.mq.rocketmq.property.RocketMQAutoConfigureProperty;
import ace.fw.mq.rocketmq.property.RocketMQProducerProperty;
import ace.fw.mq.serializer.Deserializer;
import ace.fw.mq.serializer.Serializer;
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
 * @description 普通消息生产者注册器, 根据配置动态注册多个消息生产者
 */
@Data
@Builder
@Slf4j
public class TransactionMQProducerRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private RocketMQAutoConfigureProperty rocketMQAutoConfigureProperty;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        ListableBeanFactory beanFactory = configurableListableBeanFactory;
        Map<String, TransactionMQListener> transactionMQListenerMap = beanFactory.getBeansOfType(TransactionMQListener.class);
        if (MapUtils.isEmpty(transactionMQListenerMap)) {
            log.info("can not find transactionMQListenerMap,exit auto configure");
            return;
        }
        transactionMQListenerMap.entrySet()
                .stream()
                .forEach(entry -> {
                    TransactionMQListener mqListener = entry.getValue();
                    String transactionMQListenerBeanName = entry.getKey();
                    Map.Entry<String, RocketMQProducerProperty> rocketMQProducerProperty = this.findTransactionRocketMQProducerProperty(transactionMQListenerBeanName);
                    if (Objects.isNull(rocketMQProducerProperty)) {
                        String msg = String.format("transaction mq listener [bean name:%s] can not find the config", transactionMQListenerBeanName);
                        throw new RuntimeException(msg);
                    }
                    this.register(mqListener, transactionMQListenerBeanName, rocketMQProducerProperty, configurableListableBeanFactory);
                });
    }

    /**
     * 根据bean name 搜索相关配置,搜索到多于一个配置,则throw {@link RuntimeException}
     *
     * @param mqListenerBeanName
     * @return
     */
    private Map.Entry<String, RocketMQProducerProperty> findTransactionRocketMQProducerProperty(String mqListenerBeanName) {
        Predicate<Map.Entry<String, RocketMQProducerProperty>> propertyMapKeyFilter = p -> StringUtils.equalsIgnoreCase(p.getKey(), mqListenerBeanName);
        Predicate<Map.Entry<String, RocketMQProducerProperty>> propertyBeanNameFilter = p -> StringUtils.equalsIgnoreCase(p.getValue().getBeanName(), mqListenerBeanName);

        List<Map.Entry<String, RocketMQProducerProperty>> properties = this.rocketMQAutoConfigureProperty
                .getMqProducer()
                .entrySet()
                .stream()
                .filter(p -> MQProducerTypeEnum.TRANSACTION_PRODUCER.equals(p.getValue().getType()))
                .filter(p -> propertyMapKeyFilter.or(propertyBeanNameFilter).test(p))
                .collect(Collectors.toList());
        if (properties.size() == 0) {
            return null;
        }
        if (properties.size() > 2) {
            String msg = String.format("[bean name:%s] transaction mq listener config must be one,but find more than one", mqListenerBeanName);
            throw new RuntimeException(msg);
        }
        return properties.get(0);
    }

    private void register(TransactionMQListener mqListener, String mqListenerBeanName, Map.Entry<String, RocketMQProducerProperty> entry, ConfigurableListableBeanFactory configurableListableBeanFactory) {
        RocketMQProducerProperty rocketMQProducerProperty = entry.getValue();
        ListableBeanFactory beanFactory = configurableListableBeanFactory;
        BeanDefinitionRegistry registry = ((BeanDefinitionRegistry) configurableListableBeanFactory);
        String transactionMQProducerBeanName = this.getTransactionMQProducerBeanName(mqListenerBeanName);

        TransactionMQProducerImpl mqProducer = TransactionMQProducerImpl
                .builder()
                .defaultSerializer(beanFactory.getBean(Serializer.class))
                .deserializer(beanFactory.getBean(Deserializer.class))
                .messageConverter(beanFactory.getBean(MessageConverter.class))
                .rocketMQMessageChecker(beanFactory.getBean(RocketMQMessageChecker.class))
                .groupName(rocketMQProducerProperty.getGroupName())
                .nameServerAddress(rocketMQProducerProperty.getNameServerAddress())
                .rocketMQTransactionExecutorServiceProperty(rocketMQProducerProperty.getTransactionExecutorServiceProperty())
                .transactionMQProducerId(transactionMQProducerBeanName)
                .transactionMqListener(mqListener)
                .build();

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(TransactionMQProducerImpl.class, () -> mqProducer);

        registry.registerBeanDefinition(transactionMQProducerBeanName, beanDefinitionBuilder.getRawBeanDefinition());
        String msg = String.format("transaction mq listener [bean name:%s] register success.", mqListenerBeanName);
        log.info(msg);
    }

    private String getTransactionMQProducerBeanName(String mqListenerBeanName) {
        return String.format("%s_TransactionMQProducer", mqListenerBeanName);
    }
}
