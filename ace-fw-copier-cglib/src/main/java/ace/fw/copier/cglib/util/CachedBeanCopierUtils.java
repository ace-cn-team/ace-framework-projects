package ace.fw.copier.cglib.util;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/3 12:31
 * @description 放入缓存，提高性能
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class CachedBeanCopierUtils {

    //    创建过的BeanCopier实例放到缓存中，下次可以直接获取，提升性能
    static final Map<String, BeanCopier> BEAN_COPIERS = new HashMap<String, BeanCopier>();

    public static <S, T> BeanCopier registerToCache(Class<S> sourceClass, Class<T> targetClass) {
        String key = genKey(sourceClass, targetClass);
        BeanCopier copier = null;
        if (!BEAN_COPIERS.containsKey(key)) {
            copier = BeanCopier.create(sourceClass, targetClass, false);
            BEAN_COPIERS.put(key, copier);
        } else {
            copier = BEAN_COPIERS.get(key);
        }
        return copier;
    }

    public static <S, T> T copy(S source, Class<T> targetClazz) {
        return copy(source, targetClazz, null);
    }

    public static <S, T> T copy(S source, Class<T> targetClazz, Converter converter) {
        if (source == null || targetClazz == null) {
            return null;
        }
        T target = null;
        try {
            target = targetClazz.newInstance();
        } catch (Exception e) {
            //ignore
            return null;
        }
        return registerAndCopy(source, target, converter);
    }

    public static <S, T> T copy(S source, T target) {
        return copy(source, target, null);
    }

    public static <S, T> T copy(S source, T target, Converter converter) {
        if (source == null || target == null) {
            return null;
        }
        return registerAndCopy(source, target, converter);
    }

    private static <S, T> T registerAndCopy(S source, T target, Converter converter) {
        try {
            BeanCopier copier = registerToCache(source.getClass(), target.getClass());
            copier.copy(source, target, converter);
            return target;
        } catch (Exception e) {
            log.error("registerAndCopy", e);
        }
        return null;
    }

    public static <S, T> List<T> copyList(List<S> sources, Class<T> target) {
        return copyList(sources, target, null, null);
    }

    public static <S, T> List<T> copyList(List<S> sources, Class<T> target, Consumer<T> consumer) {
        return copyList(sources, target, consumer, null);
    }

    public static <S, T> List<T> copyList(List<S> sources, Class<T> target, Consumer<T> consumer, Converter converter) {
        if (CollectionUtils.isEmpty(sources)) {
            //历史原因，为了兼容业务方代码
            return new ArrayList<>();
        }

        List<T> resultList = new ArrayList<>(sources.size());
        try {
            for (S o : sources) {
                if (o == null) {
                    continue;
                }
                T destObject = target.newInstance();
                copy(o, destObject, converter);
                if (consumer != null) {
                    consumer.accept(destObject);
                }
                resultList.add(destObject);
            }
            return resultList;
        } catch (Exception e) {
            log.error("copyList", e);
        }
        return resultList;
    }

    private static String genKey(Class<?> sourceClass, Class<?> targetClass) {
        return sourceClass.getName() + targetClass.getName();
    }
}
