package ace.fw.jetcache.redisson.autoconfigure;


import ace.fw.jetcache.redisson.RedissonCacheBuilder;
import com.alicp.jetcache.CacheBuilder;
import com.alicp.jetcache.CacheConfigException;
import com.alicp.jetcache.autoconfigure.ConfigTree;
import com.alicp.jetcache.autoconfigure.ExternalCacheAutoInit;
import com.alicp.jetcache.autoconfigure.JetCacheAutoConfiguration;
import com.alicp.jetcache.autoconfigure.JetCacheCondition;
import com.alicp.jetcache.external.ExternalCacheBuilder;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/28 18:03
 * @description jetcache redisson 自动注册配置
 */
@AutoConfigureBefore(value = {JetCacheAutoConfiguration.class})
@Configuration
@Conditional(RedissonCacheAutoConfigure.RedissonCondition.class)
public class RedissonCacheAutoConfigure {

    public final static String CACHE_TYPES = "redis.redisson";

    public static class RedissonCondition extends JetCacheCondition {
        public RedissonCondition() {
            super(CACHE_TYPES);
        }
    }

    @Bean
    public RedissonAutoInit redissonAutoInit() {
        return new RedissonAutoInit();
    }

    public static class RedissonAutoInit extends ExternalCacheAutoInit implements ApplicationContextAware {

        private ApplicationContext applicationContext;

        public RedissonAutoInit() {
            super(CACHE_TYPES);
        }

        @Override
        protected CacheBuilder initCache(ConfigTree ct, String cacheAreaWithPrefix) {
            Map<String, RedissonClient> beans = applicationContext.getBeansOfType(RedissonClient.class);
            if (beans == null || beans.isEmpty()) {
                throw new CacheConfigException("no RedissonClient in spring context");
            }
            RedissonClient redissonClient = beans.values().iterator().next();
            if (beans.size() > 1) {
                String beanName = ct.getProperty("beanName");
                if (beanName == null) {
                    throw new CacheConfigException(
                            "beanName is required, because there is multiple RedissonClient in Spring context");
                }
                if (!beans.containsKey(beanName)) {
                    throw new CacheConfigException("there is no RedissonClient named "
                            + beanName + " in Spring context");
                }
                redissonClient = beans.get(beanName);
            }
            ExternalCacheBuilder builder = RedissonCacheBuilder.createBuilder()
                    .redissonClient(redissonClient);
            parseGeneralConfig(builder, ct);
            return builder;
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }
    }
}
