package ace.fw.restful.base.api.model.select;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class Select<TSelect extends Select> {
    private List<String> properties = new ArrayList<>(10);

    public TSelect select(String... property) {
        this.properties.addAll(Arrays.asList(property));
        return self();
    }

    public TSelect select(String property) {
        properties.addAll(Arrays.asList(property));
        return self();
    }

    protected TSelect self() {
        return (TSelect) this;
    }
}
