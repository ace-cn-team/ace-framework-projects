package ace.fw.json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/16 18:40
 * @description
 */
public class JacksonUtils {

    public static <T> Object parseObject(String json, Class<T> cl) {
        try {
            return getObjectMapper().readValue(json, cl);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(String json, Type type) {
        TypeReference<T> typeReference = new TypeReference<T>() {
            @Override
            public Type getType() {
                return type;
            }
        };
        try {
            return getObjectMapper().readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJson(Object value) {
        try {
            return getObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <V, K> Map<K, V> parseToMap(String json, Class<K> clk, Class<V> clv) {
        TypeReference<Map<K, V>> typeReference = new TypeReference<Map<K, V>>() {
        };
        try {
            return getObjectMapper().readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseArray(String json, Class<T> cls) {
        TypeReference<List<T>> typeReference = new TypeReference<>() {
        };
        try {
            return getObjectMapper().readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper getObjectMapper() {
        return ObjectMapperFactory.getDefaultObjectMapper();
    }

}
