package ace.fw.restful.base.api.model.orderby;

import ace.fw.restful.base.api.model.entity.EntityPropertyFunction;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/30 22:14
 * @description
 */
public interface EntitySort extends Sort {

    <T, R> void setProperty(EntityPropertyFunction<T, R> entityPropertyFunction);


}
