package ace.fw.json.fastjson;

import ace.fw.json.JsonPlugin;

import java.io.UnsupportedEncodingException;
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
public class FastJsonPlugin implements JsonPlugin {
    @Override
    public <T> T toObject(String json, Class<T> cl) {
        Object value = FastJsonUtils.parseObject(json, cl);
        if (Objects.isNull(value)) {
            return null;
        }
        return (T) value;
    }

    @Override
    public <T> T toObject(String json, Type type) {
        return FastJsonUtils.parseObject(json, type);
    }

    @Override
    public String toJson(Object value) {
        return FastJsonUtils.toJson(value);
    }

    @Override
    public <K, V> Map<K, V> toMap(String json, Class<K> clk, Class<V> clv) {
        return FastJsonUtils.parseToMap(json, clk, clv);
    }

    @Override
    public <T> List<T> toList(String json, Class<T> cls) {
        List<T> list = FastJsonUtils.parseArray(json, cls);
        return list;
    }
}
