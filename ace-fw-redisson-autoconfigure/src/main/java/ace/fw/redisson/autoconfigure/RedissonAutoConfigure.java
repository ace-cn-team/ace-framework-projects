package ace.fw.redisson.autoconfigure;

import ace.fw.json.jackson.ObjectMapperFactory;
import ace.fw.redisson.RedissonClientFactory;
import ace.fw.redisson.constant.RedissonClientConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.BaseCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/5 14:38
 * @description
 */
@ConditionalOnProperty(
        name = RedissonClientConstants.CONFIG_KEY_DEFAULT_REDISSON_CLIENT_ENABLE,
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties({RedisProperties.class})
@Configuration
public class RedissonAutoConfigure {
    @Autowired
    private RedisProperties redisProperties;

    @Bean
    @ConditionalOnMissingBean
    public RedissonClient redissonClient() {
        RedissonClient redissonClient = RedissonClientFactory.createRedissonClient(redisProperties, this.codec());
        return redissonClient;
    }

    @Bean
    @ConditionalOnMissingBean
    public BaseCodec codec() {
        ObjectMapper objectMapper = ObjectMapperFactory.getDefaultObjectMapper().copy();
        BaseCodec codec = new JsonJacksonCodec(objectMapper);
        return codec;
    }
}
