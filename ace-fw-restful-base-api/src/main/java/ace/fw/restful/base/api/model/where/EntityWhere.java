package ace.fw.restful.base.api.model.where;

import ace.fw.restful.base.api.enums.RelationalOpEnum;
import ace.fw.restful.base.api.model.EntityPropertyFunction;
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
public class EntityWhere extends Where<EntityWhere> {

    protected <T, R> EntityWhere addCondition(boolean isAddCondition, EntityPropertyFunction<T, R> entityPropertyFunction, RelationalOpEnum relationalOp, Object value) {
        String propertyName = EntityMetaUtils.getPropertyName(entityPropertyFunction);
        this.addCondition(isAddCondition, propertyName, relationalOp, value);
        return self();
    }

    public <T, R> EntityWhere eq(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.EQ, value);
        return self();
    }

    public <T, R> EntityWhere ne(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.NE, value);
        return self();
    }

    public <T, R> EntityWhere like(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.LIKE, value);
        return self();
    }

    public <T, R> EntityWhere notLike(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.NOT_LIKE, value);
        return self();
    }

    public <T, R> EntityWhere likeRight(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.LIKE_RIGHT, value);
        return self();
    }

    public <T, R> EntityWhere gt(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.GT, value);
        return self();
    }

    public <T, R> EntityWhere ge(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.GE, value);
        return self();
    }

    public <T, R> EntityWhere lt(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.LT, value);
        return self();
    }

    public <T, R> EntityWhere le(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.LE, value);
        return self();
    }

    public <T, R> EntityWhere isNull(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.IS_NULL, value);
        return self();
    }

    public <T, R> EntityWhere isNotNull(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.IS_NOT_NULL, value);
        return self();
    }

    public <T, R> EntityWhere between(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> values) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.BETWEEN, values);
        return self();
    }

    public <T, R> EntityWhere notBetween(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> values) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.NOT_BETWEEN, values);
        return self();
    }

    public <T, R> EntityWhere in(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.IN, value);
        return self();
    }

    public <T, R> EntityWhere notIn(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> values) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.NOT_IN, values);
        return self();
    }

}
