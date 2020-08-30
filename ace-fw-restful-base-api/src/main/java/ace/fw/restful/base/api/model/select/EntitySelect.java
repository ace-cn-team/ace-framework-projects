package ace.fw.restful.base.api.model.select;

import ace.fw.restful.base.api.model.entity.EntityPropertyFunction;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 14:14
 * @description 选择字段
 */
public interface EntitySelect<TSelect extends Select> extends Select<TSelect> {

    <T, R> EntitySelect select(EntityPropertyFunction<T, R>... entityPropertyFunctions);

    <T, R> EntitySelect selectFunc(List<EntityPropertyFunction<T, R>> entityPropertyFunctions);
}
