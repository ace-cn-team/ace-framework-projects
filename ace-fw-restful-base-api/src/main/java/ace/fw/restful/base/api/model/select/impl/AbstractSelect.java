package ace.fw.restful.base.api.model.select.impl;

import ace.fw.restful.base.api.model.select.Select;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 14:14
 * @description 选择字段
 */
@Data
public abstract class AbstractSelect<TSelect extends AbstractSelect<TSelect>> implements Select<TSelect> {
    private List<String> properties = new ArrayList<>(10);

    @Override
    public TSelect select(String... property) {
        this.properties.addAll(Arrays.asList(property));
        return self();
    }

    @Override
    public TSelect select(List<String> properties) {
        this.properties.addAll(properties);
        return self();
    }
}
