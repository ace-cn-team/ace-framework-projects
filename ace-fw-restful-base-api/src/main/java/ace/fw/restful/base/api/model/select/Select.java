package ace.fw.restful.base.api.model.select;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 14:14
 * @description 选择字段
 */
public interface Select<TSelect extends Select> {

    List<String> getProperties();

    TSelect select(String... property);

    TSelect select(List<String> properties);

    default TSelect self() {
        return (TSelect) this;
    }
}
