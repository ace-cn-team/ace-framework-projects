package ace.fw.data.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 14:38
 * @description
 */
@Data
@Accessors(chain = true)
public class GenericCondition<T> {
    /**
     * 逻辑运算符
     */
    private String logicalOp = LogicalOpConstVal.AND;
    /**
     * 关系运算符
     */
    private String relationalOp = RelationalOpConstVal.EQ;
    /**
     * 字段
     */
    private String field;
    /**
     * 第一个值
     */
    private T value;
    /**
     * 第二个值
     */
    private T otherValue;
}
