package ace.fw.copier.cglib.autoconfig;

import ace.fw.copier.BeanCopier;
import ace.fw.copier.cglib.impl.CachedBeanCopier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/3 12:57
 * @description
 */
@Configuration
public class CglibBeanCopierAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public BeanCopier beanCopier() {
        return new CachedBeanCopier();
    }
}