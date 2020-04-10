package ace.fw.mq.rocketmq.autoconfigure;

import ace.fw.json.JsonPlugin;
import ace.fw.json.fastjson.FastJsonPlugin;
import ace.fw.mq.rocketmq.autoconfigure.property.RocketMQAutoConfigureProperty;
import ace.fw.mq.rocketmq.autoconfigure.register.MQProducerRegistryPostProcessor;
import ace.fw.mq.rocketmq.impl.JsonDeserializerImpl;
import ace.fw.mq.rocketmq.impl.JsonSerializerImpl;
import ace.fw.mq.serializer.Deserializer;
import ace.fw.mq.serializer.Serializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 17:53
 * @description
 */
@ConditionalOnProperty(
        value = "ace.rocketmq.enable",
        matchIfMissing = true
)
@Configuration
@EnableConfigurationProperties(RocketMQAutoConfigureProperty.class)
public class RocketMQAutoConfigure {

    @Bean
    @ConditionalOnMissingBean(MQProducerRegistryPostProcessor.class)
    public MQProducerRegistryPostProcessor mqProducerRegistryPostProcessor(RocketMQAutoConfigureProperty property) {
        return MQProducerRegistryPostProcessor
                .builder()
                .rocketMQAutoConfigureProperty(property)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(JsonPlugin.class)
    public JsonPlugin jsonPlugin() {
        return new FastJsonPlugin();
    }

    @Bean
    @ConditionalOnMissingBean(Serializer.class)
    public Serializer defaultSerializer(JsonPlugin jsonPlugin) {
        return JsonSerializerImpl
                .builder()
                .jsonPlugin(jsonPlugin)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(Deserializer.class)
    public Deserializer defaultDeserializer(JsonPlugin jsonPlugin) {
        return JsonDeserializerImpl
                .builder()
                .jsonPlugin(jsonPlugin)
                .build();
    }
}
