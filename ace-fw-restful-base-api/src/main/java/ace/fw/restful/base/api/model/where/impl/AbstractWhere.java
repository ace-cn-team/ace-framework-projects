package ace.fw.restful.base.api.model.where.impl;

import ace.fw.restful.base.api.enums.LogicalOpEnum;
import ace.fw.restful.base.api.enums.RelationalOpEnum;
import ace.fw.restful.base.api.model.where.Where;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 14:27
 * @description
 */
public abstract class AbstractWhere<TWhere extends AbstractWhere<TWhere>> implements Where<TWhere> {
    /**
     * 当前操作是否and逻辑操作
     */
    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private boolean isOrLogicalOp = false;
    @Getter
    @Setter
    private List<Condition> conditions = new ArrayList<>(5);

    @Override
    public TWhere eq(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.EQ, value);
        return self();
    }

    @Override
    public TWhere ne(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.NE, value);
        return self();
    }

    @Override
    public TWhere like(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.LIKE, value);
        return self();
    }

    @Override
    public TWhere notLike(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.NOT_LIKE, value);
        return self();
    }

    @Override
    public TWhere likeRight(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.LIKE_RIGHT, value);
        return self();
    }

    @Override
    public TWhere gt(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.GT, value);
        return self();
    }

    @Override
    public TWhere ge(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.GE, value);
        return self();
    }

    @Override
    public TWhere lt(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.LT, value);
        return self();
    }

    @Override
    public TWhere le(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.LE, value);
        return self();
    }

    @Override
    public TWhere isNull(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.IS_NULL, value);
        return self();
    }

    @Override
    public TWhere isNotNull(String propertyName, Object value) {
        this.addCondition(true, propertyName, RelationalOpEnum.IS_NOT_NULL, value);
        return self();
    }

    @Override
    public TWhere between(String propertyName, List<Object> values) {
        this.addCondition(true, propertyName, RelationalOpEnum.BETWEEN, values);
        return self();
    }

    @Override
    public TWhere notBetween(String propertyName, List<Object> values) {
        this.addCondition(true, propertyName, RelationalOpEnum.NOT_BETWEEN, values);
        return self();
    }

    @Override
    public TWhere in(String propertyName, List<Object> values) {
        this.addCondition(true, propertyName, RelationalOpEnum.IN, values);
        return self();
    }

    @Override
    public TWhere notIn(String propertyName, List<Object> values) {
        this.addCondition(true, propertyName, RelationalOpEnum.NOT_IN, values);
        return self();
    }

    @Override
    public TWhere and() {
        isOrLogicalOp = false;
        return self();
    }

    @Override
    public TWhere or() {
        isOrLogicalOp = true;
        return self();
    }

    protected TWhere addCondition(boolean isAddCondition, String propertyName, RelationalOpEnum relationalOp, Object value) {
        LogicalOpEnum logicalOp = LogicalOpEnum.AND;

        if (isOrLogicalOp) {
            isOrLogicalOp = false;
            logicalOp = LogicalOpEnum.OR;
        }

        if (isAddCondition == false) {
            return self();
        }

        Condition condition = Condition.create(logicalOp, propertyName, relationalOp, value);

        conditions.add(condition);

        return self();
    }


}
