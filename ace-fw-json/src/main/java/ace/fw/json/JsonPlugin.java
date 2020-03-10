package ace.fw.json;

import java.util.List;
import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/17  10:40
 * @description JSON插件入口
 */
public interface JsonPlugin {
    /**
     * JSON字符串转换成对象
     *
     * @param json
     * @param <T>
     * @return
     */
    <T> T toObject(String json, Class<T> cl);

    /**
     * 对象转换成JSON字符串
     *
     * @param value
     * @return
     */
    String toJson(Object value);

    /**
     * JSON字符串转成对象
     *
     * @param json
     * @return
     */
    <K, V> Map<K, V> toMap(String json, Class<K> clk, Class<V> clv);

    /**
     * JSON字符串转成对象List
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    <T> List<T> toList(String json, Class<T> cls);
}
