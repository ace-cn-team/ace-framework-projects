package ace.fw.restful.base.api.util;

import ace.fw.restful.base.api.model.EntityPropertyFunction;
import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/28 13:58
 * @description
 */
@Slf4j
public class EntityMetaUtils {
    private static Map<Class, SerializedLambda> CLASS_LAMDBA_CACHE = new ConcurrentHashMap<>(10);
    private static final String METHOD_WRITE_REPLACE = "writeReplace";


    /**
     * 获取实体的属性名称
     *
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> String getPropertyName(EntityPropertyFunction<T, R> function) {
        SerializedLambda serializedLambda = getSerializedLambda(function);
        String propertyName = methodToProperty(serializedLambda.getImplMethodName());
        return propertyName;
    }

    public static <T, R> SerializedLambda getSerializedLambda(EntityPropertyFunction<T, R> fn) {
        SerializedLambda lambda = CLASS_LAMDBA_CACHE.get(fn.getClass());
        if (lambda == null) {
            try {
                Method method = fn.getClass().getDeclaredMethod(METHOD_WRITE_REPLACE);
                method.setAccessible(Boolean.TRUE);
                lambda = (SerializedLambda) method.invoke(fn);
                CLASS_LAMDBA_CACHE.put(fn.getClass(), lambda);
            } catch (Exception ex) {
                log.error("无法解析lambda", ex);
            }
        }
        return lambda;
    }

    public static String methodToProperty(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else if (name.startsWith("get") || name.startsWith("set")) {
            name = name.substring(3);
        } else {
            throw new RuntimeException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
        }

        if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }
}
