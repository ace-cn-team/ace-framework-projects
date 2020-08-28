package ace.fw.restful.base.api.model;

import ace.fw.restful.base.api.enums.LogicalOpEnum;
import ace.fw.restful.base.api.enums.RelationalOpEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 14:38
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Condition {
    /**
     * 逻辑运算符
     */
    private LogicalOpEnum logicalOp = LogicalOpEnum.AND;
    /**
     * 关系运算符
     */
    private RelationalOpEnum relationalOp = RelationalOpEnum.EQ;
    /**
     * 属性名称
     */
    private String propertyName;
    /**
     * 比较的值，类型只支持普通值类型
     */
    private List<Object> values = new ArrayList<>(10);

    public Object getFirstValue() {
        return CollectionUtils.isEmpty(values) ? null : values.get(0);
    }

    /**
     * 避免JSON反序列化时候，把values的值覆盖了
     * @param value
     */
    public void firstValue(Object value) {
        this.values = Arrays.asList(value);
    }


    public static Condition create(LogicalOpEnum logicalOp, String propertyName, RelationalOpEnum relationalOp, Object value) {
        Condition condition = null;
        if (RelationalOpEnum.EQ.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .values(Arrays.asList(value))
                    .build();
        } else if (RelationalOpEnum.NE.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .values(Arrays.asList(value))
                    .build();
        } else if (RelationalOpEnum.GT.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .values(Arrays.asList(value))
                    .build();
        } else if (RelationalOpEnum.GE.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .values(Arrays.asList(value))
                    .build();
        } else if (RelationalOpEnum.LT.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .values(Arrays.asList(value))
                    .build();
        } else if (RelationalOpEnum.LE.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .values(Arrays.asList(value))
                    .build();
        } else if (RelationalOpEnum.LIKE.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .values(Arrays.asList(value))
                    .build();
        } else if (RelationalOpEnum.LIKE_LEFT.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .values(Arrays.asList(value))
                    .build();
        } else if (RelationalOpEnum.LIKE_RIGHT.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .values(Arrays.asList(value))
                    .build();
        } else if (RelationalOpEnum.NOT_LIKE.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .values(Arrays.asList(value))
                    .build();
        } else if (RelationalOpEnum.IS_NULL.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .build();
        } else if (RelationalOpEnum.IS_NOT_NULL.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .build();
        } else if (RelationalOpEnum.BETWEEN.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .values((List) value)
                    .build();
        } else if (RelationalOpEnum.NOT_BETWEEN.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .values((List) value)
                    .build();
        } else if (RelationalOpEnum.IN.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .values((List) value)
                    .build();
        } else if (RelationalOpEnum.NOT_IN.equals(relationalOp)) {
            condition = Condition.builder()
                    .logicalOp(logicalOp)
                    .propertyName(propertyName)
                    .relationalOp(relationalOp)
                    .values((List) value)
                    .build();
        } else {
            throw new RuntimeException("不支持的RelationalOp操作," + relationalOp.getDesc());
        }
        return condition;
    }
}
