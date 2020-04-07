package ace.fw.graphql.gateway.federation.facade;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/2 17:13
 * @description
 */
public final class DefaultServiceFactory {
    private static java.util.concurrent.ConcurrentMap<String, Object> serviceMap = new ConcurrentHashMap<>();

    public static <T> T get(String url, Class<T> cls) {
        Object preValue = serviceMap.get(url);
        if (Objects.nonNull(preValue)) {
            return (T) preValue;
        }
        T service = null;
        synchronized (DefaultServiceFactory.class) {
            if (serviceMap.containsKey(url)) {
                return (T) serviceMap.get(url);
            }
            service = Feign.builder()
                    .encoder(new GsonEncoder())
                    .decoder(new GsonDecoder())
                    .target(cls, url);
            serviceMap.put(url, service);
        }
        return service;
    }
}
