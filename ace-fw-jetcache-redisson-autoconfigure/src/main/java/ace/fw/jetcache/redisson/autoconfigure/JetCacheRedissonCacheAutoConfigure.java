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
@PropertySource("classpath:application-jetcache-redisson.properties")
@Configuration
public class JetCacheRedissonCacheAutoConfigure {

}
