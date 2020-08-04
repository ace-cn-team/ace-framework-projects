package ace.fw.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

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
    private String id;
    private String remark;
    private List<EntityProperty> properties;
}
