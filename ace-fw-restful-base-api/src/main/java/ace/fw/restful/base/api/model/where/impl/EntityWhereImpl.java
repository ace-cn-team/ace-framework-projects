package ace.fw.restful.base.api.model.where.impl;

import ace.fw.restful.base.api.enums.RelationalOpEnum;
import ace.fw.restful.base.api.model.entity.EntityPropertyFunction;
import ace.fw.restful.base.api.model.where.EntityWhere;
import ace.fw.restful.base.api.util.EntityMetaUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 14:27
 * @description
 */
@Slf4j
public class EntityWhereImpl extends AbstractWhere<EntityWhereImpl> implements EntityWhere<EntityWhereImpl> {

    protected <T, R> EntityWhereImpl addCondition(boolean isAddCondition, EntityPropertyFunction<T, R> entityPropertyFunction, RelationalOpEnum relationalOp, Object value) {
        String propertyName = EntityMetaUtils.getPropertyName(entityPropertyFunction);
        this.addCondition(isAddCondition, propertyName, relationalOp, value);
        return self();
    }

    @Override
    public <T, R> EntityWhereImpl eq(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.EQ, value);
        return self();
    }

    @Override
    public <T, R> EntityWhereImpl ne(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.NE, value);
        return self();
    }

    @Override
    public <T, R> EntityWhereImpl like(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.LIKE, value);
        return self();
    }

    @Override
    public <T, R> EntityWhereImpl notLike(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.NOT_LIKE, value);
        return self();
    }

    @Override
    public <T, R> EntityWhereImpl likeRight(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.LIKE_RIGHT, value);
        return self();
    }

    @Override
    public <T, R> EntityWhereImpl gt(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.GT, value);
        return self();
    }

    @Override
    public <T, R> EntityWhereImpl ge(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.GE, value);
        return self();
    }

    @Override
    public <T, R> EntityWhereImpl lt(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.LT, value);
        return self();
    }

    @Override
    public <T, R> EntityWhereImpl le(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.LE, value);
        return self();
    }

    @Override
    public <T, R> EntityWhereImpl isNull(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.IS_NULL, value);
        return self();
    }

    @Override
    public <T, R> EntityWhereImpl isNotNull(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.IS_NOT_NULL, value);
        return self();
    }

    @Override
    public <T, R> EntityWhereImpl between(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> values) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.BETWEEN, values);
        return self();
    }

    @Override
    public <T, R> EntityWhereImpl notBetween(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> values) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.NOT_BETWEEN, values);
        return self();
    }

    @Override
    public <T, R> EntityWhereImpl in(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.IN, value);
        return self();
    }

    @Override
    public <T, R> EntityWhereImpl notIn(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> values) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.NOT_IN, values);
        return self();
    }

}
