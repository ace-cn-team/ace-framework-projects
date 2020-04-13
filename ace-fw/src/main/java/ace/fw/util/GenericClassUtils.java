package ace.fw.util;

import java.lang.reflect.ParameterizedType;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/11 16:16
 * @description
 */
public abstract class GenericClassUtils<T> {
    public Class<T> getGenericClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}