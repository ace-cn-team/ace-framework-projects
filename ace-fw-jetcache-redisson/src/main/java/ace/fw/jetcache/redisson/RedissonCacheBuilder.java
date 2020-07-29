package ace.fw.jetcache.redisson;

import com.alicp.jetcache.external.ExternalCacheBuilder;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/28 18:03
 * @description
 */
public class RedissonCacheBuilder<T extends ExternalCacheBuilder<T>> extends ExternalCacheBuilder<T> {
    public static class RedissonCacheBuilderImpl extends RedissonCacheBuilder<RedissonCacheBuilderImpl> {
    }

    public static RedissonCacheBuilderImpl createBuilder() {
        return new RedissonCacheBuilderImpl();
    }

    protected RedissonCacheBuilder() {
        buildFunc(config -> new RedissonCache((RedissonCacheConfig) config));
    }

    @Override
    public RedissonCacheConfig getConfig() {
        if (config == null) {
            config = new RedissonCacheConfig();
        }
        return (RedissonCacheConfig) config;
    }

    public T redissonClient(RedissonClient redissonClient) {
        getConfig().setRedissonClient(redissonClient);
        return self();
    }
}
