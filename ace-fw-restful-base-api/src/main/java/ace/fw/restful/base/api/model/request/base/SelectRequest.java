package ace.fw.restful.base.api.model.request.base;

import ace.fw.restful.base.api.model.entity.EntityPropertyFunction;
import ace.fw.restful.base.api.model.select.EntitySelect;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectRequest implements EntitySelect<SelectRequest> {
    private List<String> properties = new ArrayList<>(10);

    @Override
    public SelectRequest select(String... property) {
        this.properties.addAll(Arrays.asList(property));
        return self();
    }

    @Override
    public SelectRequest select(List<String> properties) {
        this.properties.addAll(properties);
        return self();
    }

    @Override
    public <T, R> SelectRequest select(EntityPropertyFunction<T, R>... entityPropertyFunctions) {
        String[] propertyNames = Arrays.asList(entityPropertyFunctions).stream()
                .map(p -> EntityMetaUtils.getPropertyName(p))
                .toArray(String[]::new);
        this.select(propertyNames);
        return self();
    }

    @Override
    public <T, R> SelectRequest selectFunc(List<EntityPropertyFunction<T, R>> entityPropertyFunctions) {
        List<String> properties = entityPropertyFunctions.stream()
                .map(p -> EntityMetaUtils.getPropertyName(p))
                .collect(Collectors.toList());
        this.select(properties);
        return self();
    }
}
