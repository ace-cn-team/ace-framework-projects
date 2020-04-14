package ace.fw.mq.rocketmq.autoconfigure.register.producer;

import ace.fw.mq.enums.MQProducerTypeEnum;
import ace.fw.mq.producer.TransactionMQChecker;
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
        if (MapUtils.isEmpty(rocketMQAutoConfigureProperty.getMqProducer())) {
            log.info("can not find transaction mq config,exit auto configure");
            return;
        }
        ListableBeanFactory beanFactory = configurableListableBeanFactory;
        rocketMQAutoConfigureProperty
                .getMqProducer()
                .entrySet()
                .stream()
                .filter(p -> MQProducerTypeEnum.TRANSACTION_PRODUCER.equals(p.getValue().getType()))
                .forEach(entry -> {
                    String defaultTransactionMQCheckerBeanName = entry.getKey();
                    RocketMQProducerProperty rocketMQProducerProperty = entry.getValue();
                    String transactionMQCheckerBeanName = StringUtils.isNoneBlank(rocketMQProducerProperty.getBeanName()) ?
                            rocketMQProducerProperty.getBeanName() : defaultTransactionMQCheckerBeanName;
                    this.register(transactionMQCheckerBeanName, rocketMQProducerProperty, configurableListableBeanFactory);
                });
    }


    private void register(String mqCheckerBeanName, RocketMQProducerProperty rocketMQProducerProperty, ConfigurableListableBeanFactory configurableListableBeanFactory) {

        BeanDefinitionRegistry registry = ((BeanDefinitionRegistry) configurableListableBeanFactory);
        String transactionMQProducerBeanName = this.getTransactionMQProducerBeanName(mqCheckerBeanName);

        TransactionMQProducerImpl mqProducer = TransactionMQProducerImpl
                .builder()
//                .defaultSerializer(beanFactory.getBean(Serializer.class))
             // .deserializer(beanFactory.getBean(Deserializer.class))
//                .messageConverter(beanFactory.getBean(MessageConverter.class))
//                .rocketMQMessageChecker(beanFactory.getBean(RocketMQMessageChecker.class))
//                .transactionMqChecker(mqChecker)
                .groupName(rocketMQProducerProperty.getGroupName())
                .nameServerAddress(rocketMQProducerProperty.getNameServerAddress())
                .rocketMQTransactionExecutorServiceProperty(rocketMQProducerProperty.getTransactionExecutorServiceProperty())
                .transactionMQProducerId(transactionMQProducerBeanName)
                .build();

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(TransactionMQProducerImpl.class, () -> mqProducer)
                .addAutowiredProperty("defaultDeserializer")
                .addAutowiredProperty("defaultSerializer")
                .addAutowiredProperty("messageConverter")
                .addAutowiredProperty("rocketMQMessageChecker")
                .addPropertyReference("transactionMqChecker", mqCheckerBeanName);

        registry.registerBeanDefinition(transactionMQProducerBeanName, beanDefinitionBuilder.getRawBeanDefinition());
        String msg = String.format("transaction mq listener [bean name:%s] register success.", mqCheckerBeanName);
        log.info(msg);
    }

    private String getTransactionMQProducerBeanName(String mqCheckerBeanName) {
        return String.format("%s_TransactionMQProducer", mqCheckerBeanName);
    }
}
