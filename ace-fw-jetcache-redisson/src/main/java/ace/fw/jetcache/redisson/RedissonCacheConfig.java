package ace.fw.jetcache.redisson;

import com.alicp.jetcache.external.ExternalCacheConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/28 18:03
 * @description
 */
public class RedissonCacheConfig<K, V> extends ExternalCacheConfig<K, V> {
    private RedissonClient redissonClient;

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}
