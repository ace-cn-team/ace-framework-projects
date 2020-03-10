package ace.fw.data.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 14:27
 * @description
 */
@Data
@Accessors(chain = true)
public class GenericWhere<T> {
    /**
     * 当前操作是否and逻辑操作
     */
    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private boolean isOrLogicalOp = false;
    private List<GenericCondition<T>> conditions = new ArrayList<>(5);

    public GenericWhere<T> eq(String field, T value) {
        return eq(true, field, value);
    }

    public GenericWhere<T> eqValueNotNull(String field, T value) {
        eq(value != null, field, value);
        return this;
    }

    public GenericWhere eq(boolean condition, String field, T value) {
        this.addCondition(condition, field, RelationalOpConstVal.EQ, value);
        return this;
    }


    public GenericWhere and() {
        isOrLogicalOp = false;
        return this;
    }

    public GenericWhere or() {
        isOrLogicalOp = true;
        return this;
    }

    private void addCondition(boolean condition, String field, String relationalOp, T value) {
        String logicalOp = LogicalOpConstVal.AND;
        if (isOrLogicalOp) {
            isOrLogicalOp = false;
            logicalOp = LogicalOpConstVal.OR;
        }
        if (condition == false) {
            return;
        }
        conditions.add(new GenericCondition<T>()
                .setLogicalOp(logicalOp)
                .setField(field)
                .setRelationalOp(relationalOp)
                .setValue(value)
        );
    }
}
