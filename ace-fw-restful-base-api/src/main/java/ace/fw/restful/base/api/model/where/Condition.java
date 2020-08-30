package ace.fw.restful.base.api.model.where;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/30 21:58
 * @description
 */
public interface Condition {
    Object getFirstValue();

    void firstValue(Object value);

    ace.fw.restful.base.api.enums.LogicalOpEnum getLogicalOp();

    ace.fw.restful.base.api.enums.RelationalOpEnum getRelationalOp();

    String getPropertyName();

    java.util.List<Object> getValues();

    void setLogicalOp(ace.fw.restful.base.api.enums.LogicalOpEnum logicalOp);

    void setRelationalOp(ace.fw.restful.base.api.enums.RelationalOpEnum relationalOp);

    void setPropertyName(String propertyName);

    void setValues(java.util.List<Object> values);
}
