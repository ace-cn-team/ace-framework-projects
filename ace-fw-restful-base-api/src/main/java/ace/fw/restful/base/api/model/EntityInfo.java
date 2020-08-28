package ace.fw.restful.base.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

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
public class EntityInfo {
    /**
     * 实体类的class.name
     */
    private String id;
    /**
     * 实体备注
     */
    private String remark;
    /**
     * 实体类的属性元数据
     */
    private List<EntityProperty> properties;
}
