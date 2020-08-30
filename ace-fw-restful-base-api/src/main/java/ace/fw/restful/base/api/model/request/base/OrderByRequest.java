package ace.fw.restful.base.api.model.request.base;

import ace.fw.restful.base.api.model.entity.EntityPropertyFunction;
import ace.fw.restful.base.api.model.orderby.EntityOrderBy;
import ace.fw.restful.base.api.util.EntityMetaUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 15:50
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderByRequest implements EntityOrderBy<OrderByRequest, SortRequest> {
    private List<SortRequest> sorts = new ArrayList(10);

    public OrderByRequest add(String propertyName, boolean asc) {
        sorts.add(SortRequest.builder()
                .asc(asc)
                .property(propertyName)
                .build()
        );
        return self();
    }

    @Override
    public OrderByRequest asc(String propertyName) {
        this.add(propertyName, true);
        return self();
    }

    @Override
    public OrderByRequest desc(String propertyName) {
        this.add(propertyName, false);
        return self();
    }

    @Override
    public <T, R> OrderByRequest add(EntityPropertyFunction<T, R> entityPropertyFunction, boolean asc) {
        this.add(EntityMetaUtils.getPropertyName(entityPropertyFunction), asc);
        return self();
    }

    @Override
    public <T, R> OrderByRequest asc(EntityPropertyFunction<T, R> entityPropertyFunction) {
        this.asc(EntityMetaUtils.getPropertyName(entityPropertyFunction));
        return self();
    }

    @Override
    public <T, R> OrderByRequest desc(EntityPropertyFunction<T, R> entityPropertyFunction) {
        this.desc(EntityMetaUtils.getPropertyName(entityPropertyFunction));
        return self();
    }
}
