package ace.fw.restful.base.api.model.select;

import ace.fw.restful.base.api.model.EntityPropertyFunction;
import ace.fw.restful.base.api.util.EntityMetaUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 14:14
 * @description 选择字段
 */
@Data
public class EntitySelect extends Select<EntitySelect> {


    public <T, R> EntitySelect select(EntityPropertyFunction<T, R>... entityPropertyFunctions) {
        String[] propertyNames = Arrays.asList(entityPropertyFunctions).stream()
                .map(p -> EntityMetaUtils.getPropertyName(p))
                .toArray(String[]::new);
        this.select(propertyNames);
        return self();
    }
}
