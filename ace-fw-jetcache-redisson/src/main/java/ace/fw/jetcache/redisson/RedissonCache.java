package ace.fw.jetcache.redisson;

import com.alicp.jetcache.*;
import com.alicp.jetcache.external.AbstractExternalCache;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/28 18:03
 * @description jetcache redisson 底层实现
 */
@Slf4j
public class RedissonCache<K, V> extends AbstractExternalCache<K, V> {
    //private Logger log = LoggerFactory.getLogger(RedissonCache.class);

    private RedissonClient redissonClient;
    private RedissonCacheConfig<K, V> config;

    private Function<Object, byte[]> valueEncoder;
    private Function<byte[], Object> valueDecoder;

    public RedissonCache(RedissonCacheConfig<K, V> config) {
        super(config);
        if (config.getRedissonClient() == null) {
            throw new IllegalArgumentException("redisson must not be null");
        }
        this.redissonClient = config.getRedissonClient();
        this.config = config;
        this.valueEncoder = config.getValueEncoder();
        this.valueDecoder = config.getValueDecoder();
    }

    @Override
    protected CacheGetResult<V> do_GET(K key) {
        try {
            byte[] newKey = buildKey(key);
            String newKeyString = this.buildKeyString(newKey);
            RBucket<byte[]> resultBytesRBucket = redissonClient.getBucket(newKeyString);
            byte[] resultBytes = resultBytesRBucket.get();
            if (resultBytes != null) {
                CacheValueHolder<V> holder = (CacheValueHolder<V>) valueDecoder.apply(resultBytes);
                if (System.currentTimeMillis() >= holder.getExpireTime()) {
                    return CacheGetResult.EXPIRED_WITHOUT_MSG;
                }
                return new CacheGetResult(CacheResultCode.SUCCESS, null, holder);
            } else {
                return CacheGetResult.NOT_EXISTS_WITHOUT_MSG;
            }
        } catch (Exception ex) {
            logError("GET", key, ex);
            return new CacheGetResult(ex);
        } finally {

        }
    }

    private List<String> buildKeyList(Set<? extends K> keys) throws UnsupportedEncodingException {
        List<String> keyStringList = new ArrayList<>(keys.size());
        for (K k : keys) {
            byte[] newKey = buildKey(k);
            String newKeyString = this.buildKeyString(newKey);
            keyStringList.add(newKeyString);
        }
        return keyStringList;
    }

    @Override
    protected MultiGetResult<K, V> do_GET_ALL(Set<? extends K> keys) {
        try {
            ArrayList<K> keyList = new ArrayList<>(keys);
            List<String> keyStringList = this.buildKeyList(keys);
            Map<K, CacheGetResult<V>> resultMap = new HashMap<>();
            if (keyStringList.size() > 0) {
                Map<String, byte[]> mgetResults = redissonClient.getBuckets().get(keyStringList.stream().toArray(String[]::new));
                for (int i = 0; i < keyStringList.size(); i++) {
                    String keyString = keyStringList.get(i);
                    K key = keyList.get(i);
                    byte[] value = mgetResults.get(keyString);
                    if (value != null) {
                        CacheValueHolder<V> holder = (CacheValueHolder<V>) valueDecoder.apply(value);
                        if (System.currentTimeMillis() >= holder.getExpireTime()) {
                            resultMap.put(key, CacheGetResult.EXPIRED_WITHOUT_MSG);
                        } else {
                            CacheGetResult<V> r = new CacheGetResult<>(CacheResultCode.SUCCESS, null, holder);
                            resultMap.put(key, r);
                        }
                    } else {
                        resultMap.put(key, CacheGetResult.NOT_EXISTS_WITHOUT_MSG);
                    }
                }
            }
            return new MultiGetResult<>(CacheResultCode.SUCCESS, null, resultMap);
        } catch (Exception ex) {
            logError("GET_ALL", "keys(" + keys.size() + ")", ex);
            return new MultiGetResult<>(ex);
        } finally {
        }
    }

    @Override
    protected CacheResult do_PUT(K key, V value, long expireAfterWrite, TimeUnit timeUnit) {
        try {
            CacheValueHolder<V> holder = new CacheValueHolder(value, timeUnit.toMillis(expireAfterWrite));
            byte[] keyBytes = buildKey(key);
            byte[] valueBytes = valueEncoder.apply(holder);
            String newKeyString = this.buildKeyString(keyBytes);
            RBucket<byte[]> rBucket = redissonClient.getBucket(newKeyString);
            rBucket.set(valueBytes, expireAfterWrite, timeUnit);
            Boolean result = true;
            if (Boolean.TRUE.equals(result)) {
                return CacheResult.SUCCESS_WITHOUT_MSG;
            } else {
                return new CacheResult(CacheResultCode.FAIL, "result:" + result);
            }
        } catch (Exception ex) {
            logError("PUT", key, ex);
            return new CacheResult(ex);
        } finally {
        }
    }

    @Override
    protected CacheResult do_PUT_ALL(Map<? extends K, ? extends V> map, long expireAfterWrite, TimeUnit timeUnit) {
        try {
            int failCount = 0;
            for (Map.Entry<? extends K, ? extends V> en : map.entrySet()) {
                CacheValueHolder<V> holder = new CacheValueHolder(en.getValue(), timeUnit.toMillis(expireAfterWrite));
                byte[] keyBytes = buildKey(en.getKey());
                byte[] valueBytes = valueEncoder.apply(holder);
                String newKeyString = this.buildKeyString(keyBytes);
                RBucket<byte[]> rBucket = redissonClient.getBucket(newKeyString);
                Boolean result = true;
                try {
                    rBucket.set(valueBytes, expireAfterWrite, timeUnit);
                } catch (Exception ex) {
                    String errorMsg = String.format("PUT_ALL, Key:%s fail", newKeyString);
                    log.error(errorMsg, ex);
                    result = false;
                }
                if (!Boolean.TRUE.equals(result)) {
                    failCount++;
                }
            }
            return failCount == 0 ? CacheResult.SUCCESS_WITHOUT_MSG :
                    failCount == map.size() ? CacheResult.FAIL_WITHOUT_MSG : CacheResult.PART_SUCCESS_WITHOUT_MSG;
        } catch (Exception ex) {
            logError("PUT_ALL", "map(" + map.size() + ")", ex);
            return new CacheResult(ex);
        } finally {
        }
    }

    @Override
    protected CacheResult do_REMOVE(K key) {
        try {
            byte[] keyBytes = buildKey(key);
            String newKeyString = buildKeyString(keyBytes);
            Boolean result = redissonClient.getBucket(newKeyString).delete();
            if (result == null) {
                return new CacheResult(CacheResultCode.FAIL, "result:" + result);
            }
            if (Boolean.TRUE.equals(result)) {
                return CacheResult.SUCCESS_WITHOUT_MSG;
            } else {
                return new CacheResult(CacheResultCode.NOT_EXISTS, null);
            }

        } catch (Exception ex) {
            logError("REMOVE", key, ex);
            return new CacheResult(ex);
        } finally {
        }
    }

    @Override
    protected CacheResult do_REMOVE_ALL(Set<? extends K> keys) {
        try {
            List<String> keyStringList = this.buildKeyList(keys);
            Long result = redissonClient.getKeys().delete(keyStringList.toArray(String[]::new));
            if (result != null) {
                return CacheResult.SUCCESS_WITHOUT_MSG;
            } else {
                return new CacheResult(CacheResultCode.FAIL, "result:" + result);
            }
        } catch (Exception ex) {
            logError("REMOVE_ALL", "keys(" + keys.size() + ")", ex);
            return new CacheResult(ex);
        } finally {

        }
    }

    @Override
    protected CacheResult do_PUT_IF_ABSENT(K key, V value, long expireAfterWrite, TimeUnit timeUnit) {
        try {
            CacheValueHolder<V> holder = new CacheValueHolder(value, timeUnit.toMillis(expireAfterWrite));
            byte[] newKey = buildKey(key);
            String newKeyString = this.buildKeyString(newKey);
            byte[] valueHolder = valueEncoder.apply(holder);
            RBucket<byte[]> rBucket = redissonClient.getBucket(newKeyString);
            Boolean result = rBucket.trySet(valueHolder, expireAfterWrite, timeUnit);
            if (Boolean.TRUE.equals(result)) {
                return CacheResult.SUCCESS_WITHOUT_MSG;
            } else {
                return CacheResult.EXISTS_WITHOUT_MSG;
            }
        } catch (Exception ex) {
            logError("PUT_IF_ABSENT", key, ex);
            return new CacheResult(ex);
        } finally {
        }
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        throw new UnsupportedOperationException("RedisRedissonCache does not support unwrap");
    }

    @Override
    public RedissonCacheConfig<K, V> config() {
        return config;
    }

    private String buildKeyString(byte[] keyByte) throws UnsupportedEncodingException {
        return new String(keyByte, "UTF-8");
    }


}
