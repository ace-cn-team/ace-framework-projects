package ace.fw.mq.rocketmq.autoconfigure.property;

import ace.fw.mq.rocketmq.autoconfigure.constant.RocketMQConfigureConstants;
import ace.fw.mq.rocketmq.constants.RocketMQConstant;
import ace.fw.mq.rocketmq.property.RocketMQProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 17:55
 * @description
 */
public final class RocketMQAutoConfigurePropertyConverter {

    /**
     * 从环境配置中，绑定配置对象，因为spring顺序，无法直接使用{@link ConfigurationProperties}绑定对象
     *
     * @param environment
     * @return
     */
    public static RocketMQAutoConfigureProperty from(Environment environment) {
        RocketMQAutoConfigureProperty rocketMQAutoConfigureProperty = Binder
                .get(environment)
                .bindOrCreate(RocketMQConfigureConstants.CONFIG_PREFIX, Bindable.of(RocketMQAutoConfigureProperty.class));

        return rocketMQAutoConfigureProperty;
    }
}
