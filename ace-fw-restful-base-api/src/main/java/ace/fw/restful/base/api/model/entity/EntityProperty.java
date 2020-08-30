package ace.fw.restful.base.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/6 18:51
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntityProperty {
    /**
     * 属性名称
     */
    private String id;
    /**
     * 属性备注
     */
    private String remark;
    /**
     * 属性类型
     */
    private Class type;
    /**
     * 属性对应的列名
     */
    private String column;
}
