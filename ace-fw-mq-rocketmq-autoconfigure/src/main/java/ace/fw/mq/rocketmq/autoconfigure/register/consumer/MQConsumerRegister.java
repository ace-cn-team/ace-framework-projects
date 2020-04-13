package ace.fw.mq.rocketmq.autoconfigure.register.consumer;

import ace.fw.mq.consumer.MQListener;
import ace.fw.mq.enums.MQConsumerTypeEnum;
import ace.fw.mq.rocketmq.autoconfigure.property.RocketMQConsumerProperty;
import ace.fw.mq.rocketmq.impl.consumer.MQConsumerImpl;
import ace.fw.mq.rocketmq.impl.producer.AbstractMQProducer;
import ace.fw.mq.rocketmq.impl.producer.MessageConverter;
import ace.fw.mq.rocketmq.impl.producer.RocketMQMessageChecker;
import ace.fw.mq.serializer.Deserializer;
import ace.fw.mq.serializer.Serializer;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 18:30
 * @description 抽象消息消费者注册器, 根据配置动态注册多个消息消费者
 */
@Data
@Accessors(chain = true)
@Slf4j
public class MQConsumerRegister implements ConsumerRegister {


    @Override
    public void register(MQListener mqListener, String mqListenerBeanName, RocketMQConsumerProperty rocketMQConsumerProperty, ListableBeanFactory beanFactory, BeanDefinitionRegistry registry) {
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
    }

    private String getMQConsumerBeanName(String mqListenerBeanName) {
        return String.format("%s_MQConsumer", mqListenerBeanName);
    }

    @Override
    public boolean isSupport(RocketMQConsumerProperty rocketMQConsumerProperty) {
        return MQConsumerTypeEnum.CONSUMER.equals(rocketMQConsumerProperty.getType());
    }
}
