package ace.fw.json.jackson;

import ace.fw.json.JsonPlugin;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/17 10:52
 * @description
 */
public class JacksonPlugin implements JsonPlugin {
    @Override
    public <T> T toObject(String json, Class<T> cl) {
        Object value = JacksonUtils.parseObject(json, cl);
        if (Objects.isNull(value)) {
            return null;
        }
        return (T) value;
    }

    @Override
    public <T> T toObject(String json, Type type) {
        return JacksonUtils.parseObject(json, type);
    }

    @Override
    public String toJson(Object value) {
        return JacksonUtils.toJson(value);
    }

    @Override
    public <K, V> Map<K, V> toMap(String json, Class<K> clk, Class<V> clv) {
        return JacksonUtils.parseToMap(json, clk, clv);
    }

    @Override
    public <T> List<T> toList(String json, Class<T> cls) {
        List<T> list = JacksonUtils.parseArray(json, cls);
        return list;
    }
}
