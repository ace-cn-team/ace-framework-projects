package ace.fw.restful.base.api.util;

import ace.fw.restful.base.api.model.entity.EntityPropertyFunction;
import ace.fw.restful.base.api.model.request.base.OrderByRequest;
import ace.fw.restful.base.api.model.request.base.SelectRequest;
import ace.fw.restful.base.api.model.request.base.WhereRequest;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/30 20:26
 * @description
 */
public class QueryUtils {
    public static SelectRequest select() {
        return new SelectRequest();
    }

    public static <T, R> SelectRequest select(EntityPropertyFunction<T, R>... entityPropertyFunctions) {
        return new SelectRequest().select(entityPropertyFunctions);
    }

    public static OrderByRequest orderBy() {
        return new OrderByRequest();
    }

    public static <T, R> OrderByRequest orderBy(EntityPropertyFunction<T, R> entityPropertyFunction, boolean asc) {
        return new OrderByRequest().add(entityPropertyFunction, asc);
    }

    public static <T, R> OrderByRequest orderByAsc(EntityPropertyFunction<T, R> entityPropertyFunction) {
        return orderBy(entityPropertyFunction, true);
    }

    public static <T, R> OrderByRequest orderByDesc(EntityPropertyFunction<T, R> entityPropertyFunction) {
        return orderBy(entityPropertyFunction, false);
    }


    public static WhereRequest where() {
        return new WhereRequest();
    }
}

