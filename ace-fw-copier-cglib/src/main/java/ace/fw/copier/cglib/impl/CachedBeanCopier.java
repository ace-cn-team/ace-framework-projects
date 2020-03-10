package ace.fw.copier.cglib.impl;

import ace.fw.copier.BeanCopier;
import ace.fw.copier.cglib.util.CachedBeanCopierUtils;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/3 12:52
 * @description
 */
public class CachedBeanCopier implements BeanCopier {
    @Override
    public <S, T> T copy(S source, Class<T> targetClazz) {
        return CachedBeanCopierUtils.copy(source, targetClazz);
    }

    @Override
    public <S, T> T copy(S source, T target) {
        return CachedBeanCopierUtils.copy(source, target);
    }

    @Override
    public <S, T> List<T> copyList(List<S> sources, Class<T> target) {
        return CachedBeanCopierUtils.copyList(sources, target);
    }
}
