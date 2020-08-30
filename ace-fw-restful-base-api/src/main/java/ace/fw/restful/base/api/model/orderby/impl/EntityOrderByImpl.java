package ace.fw.restful.base.api.model.orderby.impl;

import ace.fw.restful.base.api.model.entity.EntityPropertyFunction;
import ace.fw.restful.base.api.model.orderby.EntityOrderBy;
import ace.fw.restful.base.api.util.EntityMetaUtils;
import lombok.Data;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 15:50
 * @description
 */
@Data
public class EntityOrderByImpl extends AbstractOrderBy<EntityOrderByImpl> implements EntityOrderBy<EntityOrderByImpl> {

    @Override
    public <T, R> EntityOrderByImpl add(EntityPropertyFunction<T, R> entityPropertyFunction, boolean asc) {
        this.add(EntityMetaUtils.getPropertyName(entityPropertyFunction), asc);
        return self();
    }

    @Override
    public <T, R> EntityOrderByImpl asc(EntityPropertyFunction<T, R> entityPropertyFunction) {
        this.asc(EntityMetaUtils.getPropertyName(entityPropertyFunction));
        return self();
    }

    @Override
    public <T, R> EntityOrderByImpl desc(EntityPropertyFunction<T, R> entityPropertyFunction) {
        this.desc(EntityMetaUtils.getPropertyName(entityPropertyFunction));
        return self();
    }
}
