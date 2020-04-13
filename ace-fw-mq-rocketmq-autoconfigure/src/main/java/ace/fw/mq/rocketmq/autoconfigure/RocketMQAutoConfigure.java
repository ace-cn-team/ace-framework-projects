package ace.fw.mq.rocketmq.autoconfigure;

import ace.fw.json.JsonPlugin;
import ace.fw.json.fastjson.FastJsonPlugin;
import ace.fw.mq.rocketmq.autoconfigure.constant.RocketMQConfigureConstants;
import ace.fw.mq.rocketmq.autoconfigure.property.RocketMQAutoConfigureProperty;
import ace.fw.mq.rocketmq.autoconfigure.property.RocketMQAutoConfigurePropertyConverter;
import ace.fw.mq.rocketmq.autoconfigure.register.consumer.CommonMQConsumerRegistryPostProcessor;
import ace.fw.mq.rocketmq.autoconfigure.register.consumer.ConsumerRegister;
import ace.fw.mq.rocketmq.autoconfigure.register.consumer.MQConsumerRegister;
import ace.fw.mq.rocketmq.autoconfigure.register.producer.CommonMQProducerRegistryPostProcessor;
import ace.fw.mq.rocketmq.autoconfigure.register.producer.MQProducerRegister;
import ace.fw.mq.rocketmq.autoconfigure.register.producer.ProducerRegister;
import ace.fw.mq.rocketmq.impl.consumer.MQConsumerImpl;
import ace.fw.mq.rocketmq.impl.serializer.JsonDeserializerImpl;
import ace.fw.mq.rocketmq.impl.serializer.JsonSerializerImpl;
import ace.fw.mq.rocketmq.impl.producer.MessageConverter;
import ace.fw.mq.rocketmq.impl.producer.RocketMQMessageChecker;
import ace.fw.mq.serializer.Deserializer;
import ace.fw.mq.serializer.Serializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 17:53
 * @description
 */
@ConditionalOnProperty(
        value = RocketMQConfigureConstants.CONFIG_PREFIX + ".enable",
        matchIfMissing = true
)
@Configuration
@EnableConfigurationProperties(RocketMQAutoConfigureProperty.class)
public class RocketMQAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    public CommonMQProducerRegistryPostProcessor mqProducerRegistryPostProcessor(Environment environment) {
        RocketMQAutoConfigureProperty rocketMQAutoConfigureProperty = RocketMQAutoConfigurePropertyConverter.from(environment);
        List<ProducerRegister> producerRegisters = Arrays.asList(
                new MQProducerRegister()

        );
        return CommonMQProducerRegistryPostProcessor
                .builder()
                .rocketMQAutoConfigureProperty(rocketMQAutoConfigureProperty)
                .producerRegisters(producerRegisters)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public CommonMQConsumerRegistryPostProcessor mqConsumerRegistryPostProcessor(Environment environment) {
        RocketMQAutoConfigureProperty rocketMQAutoConfigureProperty = RocketMQAutoConfigurePropertyConverter.from(environment);
        List<ConsumerRegister> consumerRegisters = Arrays.asList(
                new MQConsumerRegister()
        );
        return CommonMQConsumerRegistryPostProcessor
                .builder()
                .rocketMQAutoConfigureProperty(rocketMQAutoConfigureProperty)
                .consumerRegisters(consumerRegisters)
                .build();
    }


    @Bean
    @ConditionalOnMissingBean
    public JsonPlugin jsonPlugin() {
        return new FastJsonPlugin();
    }

    @Bean
    @ConditionalOnMissingBean
    public Serializer defaultSerializer(JsonPlugin jsonPlugin) {
        return JsonSerializerImpl
                .builder()
                .jsonPlugin(jsonPlugin)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public Deserializer defaultDeserializer(JsonPlugin jsonPlugin) {
        return JsonDeserializerImpl
                .builder()
                .jsonPlugin(jsonPlugin)
                .build();

    }

    @Bean
    @ConditionalOnMissingBean
    public MessageConverter messageConverter() {
        return MessageConverter.builder()
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public RocketMQMessageChecker rocketMQMessageChecker(RocketMQAutoConfigureProperty rocketMQAutoConfigureProperty) {
        return RocketMQMessageChecker.builder()
                .rocketMQProperty(rocketMQAutoConfigureProperty.getCommonProperty())
                .build();
    }
}
