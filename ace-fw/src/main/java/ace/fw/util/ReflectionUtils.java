package ace.fw.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/3 10:41
 * @description
 */
@Slf4j
public final class ReflectionUtils {
    /**
     * 获取接口上的泛型T
     *
     * @param cls   接口
     * @param index 泛型索引
     */
    public static Class<?> getInterfaceGeneric(Class<?> cls, int index) {
        Type[] types = cls.getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[index];
        Type type = parameterizedType.getActualTypeArguments()[index];
        return checkType(type, index);

    }


    /**
     * 获取类上的泛型T
     *
     * @param cls   接口
     * @param index 泛型索引
     */
    public static Class<?> getClassGeneric(Class<?> cls, int index) {
        Type type = cls.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type actType = parameterizedType.getActualTypeArguments()[index];
            return checkType(actType, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                    + ", but <" + type + "> is of type " + className);
        }
    }


    private static Class<?> checkType(Type type, int index) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type t = pt.getActualTypeArguments()[index];
            return checkType(t, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                    + ", but <" + type + "> is of type " + className);
        }
    }


}
