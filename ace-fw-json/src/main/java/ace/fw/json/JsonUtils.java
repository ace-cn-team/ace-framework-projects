package ace.fw.json;

import java.util.List;
import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/16 18:40
 * @description 默认json工具
 */
public final class JsonUtils {
    private static JsonPlugin jsonPlugin;

    public static void setJsonPlugin(JsonPlugin jsonPlugin) {
        JsonUtils.jsonPlugin = jsonPlugin;
    }

    /**
     * JSON字符串转换成对象
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, Class<T> cls) {
        return jsonPlugin.toObject(json, cls);
    }

    /**
     * 对象转换成JSON字符串
     *
     * @param value
     * @return
     */
    public static String toJson(Object value) {
        return jsonPlugin.toJson(value);
    }

    /**
     * JSON字符串转成对象
     *
     * @param json
     * @return
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> clk, Class<V> clv) {
        return jsonPlugin.toMap(json, clk, clv);
    }

    /**
     * JSON字符串转成对象List
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String json, Class<T> cls) {
        return jsonPlugin.toList(json, cls);
    }
}
