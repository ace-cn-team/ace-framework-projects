package ace.fw.restful.base.api.model.orderby;

import ace.fw.restful.base.api.model.entity.EntityPropertyFunction;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 15:50
 * @description
 */
public interface EntityOrderBy<TOrderBy extends OrderBy> extends OrderBy<TOrderBy> {

    <T, R> TOrderBy add(EntityPropertyFunction<T, R> entityPropertyFunction, boolean asc);

    <T, R> TOrderBy asc(EntityPropertyFunction<T, R> entityPropertyFunction);

    <T, R> TOrderBy desc(EntityPropertyFunction<T, R> entityPropertyFunction);
}
