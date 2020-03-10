package ace.fw.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import java.time.Duration;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/17 11:54
 * @description
 */
public class RedissonClientFactory {
    public static RedissonClient createRedissonClient(RedisProperties redisProperties, Codec codec) {
        if (redisProperties.getTimeout() == null) {
            redisProperties.setTimeout(Duration.ofSeconds(10000));
        }
        Config config = new Config();
        config.setCodec(codec)
                //.setThreads()
                //.setNettyThreads()
                //.setReferenceEnabled()
                .setTransportMode(TransportMode.NIO)
                .setLockWatchdogTimeout(30 * 1000)
                .useSingleServer()
                .setAddress(redisProperties.getUrl())
                .setDatabase(redisProperties.getDatabase())
                .setPassword(redisProperties.getPassword())
                .setConnectTimeout((int) redisProperties.getTimeout().getSeconds())
        ;

        return Redisson.create(config);
    }
}
