package ace.fw.data.model;

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
public abstract class GenericWhere<TWhere extends GenericWhere, TValue> {

    /**
     * 当前操作是否and逻辑操作
     */
    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private boolean isOrLogicalOp = false;
    @Getter
    @Setter
    private List<GenericCondition<TValue>> conditions = new ArrayList<>(5);

    public TWhere eq(String field, TValue value) {
        return eq(true, field, value);
    }

    public TWhere eqValueNotNull(String field, TValue value) {
        eq(value != null, field, value);
        return self();
    }

    public TWhere eq(boolean condition, String field, TValue value) {
        this.addCondition(condition, field, RelationalOpConstVal.EQ, value);
        return self();
    }


    public TWhere and() {
        isOrLogicalOp = false;
        return self();
    }

    public TWhere or() {
        isOrLogicalOp = true;
        return self();
    }

    private TWhere addCondition(boolean condition, String field, String relationalOp, TValue value) {
        String logicalOp = LogicalOpConstVal.AND;
        if (isOrLogicalOp) {
            isOrLogicalOp = false;
            logicalOp = LogicalOpConstVal.OR;
        }
        if (condition == false) {
            return self();
        }
        conditions.add(GenericCondition.<TValue>builder()
                .logicalOp(logicalOp)
                .field(field)
                .relationalOp(relationalOp)
                .value(value)
                .build()
        );
        return self();
    }

    protected TWhere self() {
        return (TWhere) this;
    }
}
