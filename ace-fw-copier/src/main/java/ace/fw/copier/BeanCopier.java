package ace.fw.copier;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/3 12:40
 * @description
 */
public interface BeanCopier {
    <S, T> T copy(S source, Class<T> targetClazz);

    <S, T> T copy(S source, T target);

    <S, T> List<T> copyList(List<S> sources, Class<T> target);
}
