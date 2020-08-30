package ace.fw.restful.base.api.util;

import ace.fw.restful.base.api.model.entity.EntityPropertyFunction;
import ace.fw.restful.base.api.model.orderby.EntityOrderBy;
import ace.fw.restful.base.api.model.orderby.OrderBy;
import ace.fw.restful.base.api.model.orderby.impl.EntityOrderByImpl;
import ace.fw.restful.base.api.model.select.EntitySelect;
import ace.fw.restful.base.api.model.select.Select;
import ace.fw.restful.base.api.model.select.impl.EntitySelectImpl;
import ace.fw.restful.base.api.model.where.EntityWhere;
import ace.fw.restful.base.api.model.where.Where;
import ace.fw.restful.base.api.model.where.impl.EntityWhereImpl;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/30 20:26
 * @description
 */
public class QueryUtils {
    public static EntitySelectImpl select() {
        return new EntitySelectImpl();
    }

    public static <T, R> EntitySelectImpl select(EntityPropertyFunction<T, R>... entityPropertyFunctions) {
        return new EntitySelectImpl().select(entityPropertyFunctions);
    }

    public static EntityOrderByImpl orderBy() {
        return new EntityOrderByImpl();
    }

    public static <T, R> EntityOrderByImpl orderBy(EntityPropertyFunction<T, R> entityPropertyFunction, boolean asc) {
        return new EntityOrderByImpl().add(entityPropertyFunction, asc);
    }

    public static <T, R> EntityOrderByImpl orderByAsc(EntityPropertyFunction<T, R> entityPropertyFunction) {
        return orderBy(entityPropertyFunction, true);
    }

    public static <T, R> EntityOrderByImpl orderByDesc(EntityPropertyFunction<T, R> entityPropertyFunction) {
        return orderBy(entityPropertyFunction, false);
    }


    public static EntityWhereImpl where() {
        return new EntityWhereImpl();
    }
}

