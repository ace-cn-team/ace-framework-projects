package ace.fw.mq.rocketmq.autoconfigure.register.producer;

import ace.fw.mq.rocketmq.autoconfigure.property.RocketMQAutoConfigureProperty;
import ace.fw.mq.rocketmq.autoconfigure.property.RocketMQProducerProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.List;
import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 18:30
 * @description 消息生产者注册器, 根据配置动态注册多个消息生产者
 */
@Data
@Builder
@Slf4j
public class CommonMQProducerRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private RocketMQAutoConfigureProperty rocketMQAutoConfigureProperty;

    private List<ProducerRegister> producerRegisters;

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
                    this.register(rocketMQAutoConfigureProperty, entry, beanFactory);
                });

    }

    private void register(RocketMQAutoConfigureProperty rocketMQAutoConfigureProperty, Map.Entry<String, RocketMQProducerProperty> entry, ConfigurableListableBeanFactory configurableListableBeanFactory) {
        for (ProducerRegister producerRegister : producerRegisters) {
            if (producerRegister.isSupport(entry.getValue()) == false) {
                continue;
            }
            String defaultMQProducerBeanName = entry.getKey();
            RocketMQProducerProperty rocketMQProducerProperty = entry.getValue();
            BeanFactory beanFactory = configurableListableBeanFactory;
            BeanDefinitionRegistry registry = ((BeanDefinitionRegistry) configurableListableBeanFactory);
            producerRegister.register(rocketMQAutoConfigureProperty.getCommonProperty(), defaultMQProducerBeanName, rocketMQProducerProperty, beanFactory, registry);
            break;
        }
    }


}
