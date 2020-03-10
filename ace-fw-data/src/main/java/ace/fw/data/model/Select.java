package ace.fw.data.model;

import lombok.Data;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class Select {
    private List<String> fields = new ArrayList<>(10);

    public Select select(String... fields) {
        this.fields.addAll(Arrays.asList(fields));
        return this;
    }

    public Select select(String field) {
        fields.addAll(Arrays.asList(field));
        return this;
    }
}
