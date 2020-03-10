package ace.fw.data.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/6 18:51
 * @description
 */
@Data
@Accessors(chain = true)
public class EntityProperty {
    private String id;
    private String remark;
    private Class clazz;
    private String column;
}
