package ace.fw.restful.base.api.model.request.base;

import ace.fw.restful.base.api.enums.LogicalOpEnum;
import ace.fw.restful.base.api.enums.RelationalOpEnum;
import ace.fw.restful.base.api.model.entity.EntityPropertyFunction;
import ace.fw.restful.base.api.model.where.EntityWhere;
import ace.fw.restful.base.api.util.EntityMetaUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 14:27
 * @description
 */
@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WhereRequest implements EntityWhere<WhereRequest, ConditionRequest> {

    /**
     * 当前操作是否and逻辑操作
     */
    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private boolean isOrLogicalOp = false;
    @Getter
    @Setter
    private List<ConditionRequest> conditions = new ArrayList<>(5);

    @Override
    public WhereRequest eq(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.EQ, value);
        return self();
    }

    @Override
    public WhereRequest ne(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.NE, value);
        return self();
    }

    @Override
    public WhereRequest like(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.LIKE, value);
        return self();
    }

    @Override
    public WhereRequest notLike(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.NOT_LIKE, value);
        return self();
    }

    @Override
    public WhereRequest likeRight(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.LIKE_RIGHT, value);
        return self();
    }

    @Override
    public WhereRequest gt(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.GT, value);
        return self();
    }

    @Override
    public WhereRequest ge(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.GE, value);
        return self();
    }

    @Override
    public WhereRequest lt(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.LT, value);
        return self();
    }

    @Override
    public WhereRequest le(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.LE, value);
        return self();
    }

    @Override
    public WhereRequest isNull(String propertyName) {
        this.addCondition(true, propertyName, RelationalOpEnum.IS_NULL, null);
        return self();
    }

    @Override
    public WhereRequest isNotNull(String propertyName) {
        this.addCondition(true, propertyName, RelationalOpEnum.IS_NOT_NULL, null);
        return self();
    }

    @Override
    public WhereRequest between(String propertyName, List<Object> values) {
        this.addCondition(true, propertyName, RelationalOpEnum.BETWEEN, values);
        return self();
    }

    @Override
    public WhereRequest notBetween(String propertyName, List<Object> values) {
        this.addCondition(true, propertyName, RelationalOpEnum.NOT_BETWEEN, values);
        return self();
    }

    @Override
    public WhereRequest in(String propertyName, List<Object> values) {
        this.addCondition(true, propertyName, RelationalOpEnum.IN, values);
        return self();
    }

    @Override
    public WhereRequest notIn(String propertyName, List<Object> values) {
        this.addCondition(true, propertyName, RelationalOpEnum.NOT_IN, values);
        return self();
    }

    @Override
    public WhereRequest and() {
        isOrLogicalOp = false;
        return self();
    }

    @Override
    public WhereRequest or() {
        isOrLogicalOp = true;
        return self();
    }

    protected WhereRequest addCondition(boolean isAddCondition, String propertyName, RelationalOpEnum relationalOp, Object value) {
        LogicalOpEnum logicalOp = LogicalOpEnum.AND;

        if (isOrLogicalOp) {
            isOrLogicalOp = false;
            logicalOp = LogicalOpEnum.OR;
        }

        if (isAddCondition == false) {
            return self();
        }

        ConditionRequest condition = ConditionRequest.create(logicalOp, propertyName, relationalOp, value);

        conditions.add(condition);

        return self();
    }

    protected <T, R> WhereRequest addCondition(boolean isAddCondition, EntityPropertyFunction<T, R> entityPropertyFunction, RelationalOpEnum relationalOp, Object value) {
        String propertyName = EntityMetaUtils.getPropertyName(entityPropertyFunction);
        this.addCondition(isAddCondition, propertyName, relationalOp, value);
        return self();
    }

    @Override
    public <T, R> WhereRequest eq(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.EQ, value);
        return self();
    }

    @Override
    public <T, R> WhereRequest ne(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.NE, value);
        return self();
    }

    @Override
    public <T, R> WhereRequest like(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.LIKE, value);
        return self();
    }

    @Override
    public <T, R> WhereRequest notLike(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.NOT_LIKE, value);
        return self();
    }

    @Override
    public <T, R> WhereRequest likeRight(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.LIKE_RIGHT, value);
        return self();
    }

    @Override
    public <T, R> WhereRequest gt(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.GT, value);
        return self();
    }

    @Override
    public <T, R> WhereRequest ge(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.GE, value);
        return self();
    }

    @Override
    public <T, R> WhereRequest lt(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.LT, value);
        return self();
    }

    @Override
    public <T, R> WhereRequest le(EntityPropertyFunction<T, R> entityPropertyFunction, Object value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.LE, value);
        return self();
    }

    @Override
    public <T, R> WhereRequest isNull(EntityPropertyFunction<T, R> entityPropertyFunction) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.IS_NULL, null);
        return self();
    }

    @Override
    public <T, R> WhereRequest isNotNull(EntityPropertyFunction<T, R> entityPropertyFunction) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.IS_NOT_NULL, null);
        return self();
    }

    @Override
    public <T, R> WhereRequest between(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> values) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.BETWEEN, values);
        return self();
    }

    @Override
    public <T, R> WhereRequest notBetween(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> values) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.NOT_BETWEEN, values);
        return self();
    }

    @Override
    public <T, R> WhereRequest in(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> value) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.IN, value);
        return self();
    }

    @Override
    public <T, R> WhereRequest notIn(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> values) {
        this.addCondition(true, entityPropertyFunction, RelationalOpEnum.NOT_IN, values);
        return self();
    }

}
