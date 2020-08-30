package ace.fw.restful.base.api.model.request.base;

import ace.fw.restful.base.api.model.entity.EntityPropertyFunction;
import ace.fw.restful.base.api.model.orderby.EntitySort;
import ace.fw.restful.base.api.model.orderby.Sort;
import ace.fw.restful.base.api.util.EntityMetaUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:35
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SortRequest implements EntitySort {
    /**
     * 属性名称
     */
    private String property;
    /**
     * 排序方向
     */
    private Boolean asc;

    @Override
    public <T, R> void setProperty(EntityPropertyFunction<T, R> entityPropertyFunction) {
        property = EntityMetaUtils.getPropertyName(entityPropertyFunction);
    }

    @Override
    public void setProperty(String property) {
        this.property = property;
    }
}
