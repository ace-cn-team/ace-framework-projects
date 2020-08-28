package ace.fw.restful.base.api.model.orderby;

import ace.fw.restful.base.api.model.EntityPropertyFunction;
import ace.fw.restful.base.api.model.Sort;
import ace.fw.restful.base.api.util.EntityMetaUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 15:50
 * @description
 */
@Data
public class EntityOrderBy extends OrderBy<EntityOrderBy> {

    public <T, R> EntityOrderBy add(EntityPropertyFunction<T, R> entityPropertyFunction, boolean asc) {
        String propertyName = EntityMetaUtils.getPropertyName(entityPropertyFunction);
        super.add(propertyName, asc);
        return self();
    }
}
