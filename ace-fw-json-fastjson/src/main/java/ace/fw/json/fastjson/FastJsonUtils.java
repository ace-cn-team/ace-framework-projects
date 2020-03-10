package ace.fw.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ParseProcess;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.util.IOUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/16 18:40
 * @description
 */

@Slf4j
public class FastJsonUtils {

    @Getter
    private static final SerializerFeature[] defaultFeatures = new SerializerFeature[]{
            //在fastjson中，会自动检测循环引用，并且输出为fastjson专有的引用表示格式。但这个不能被其他JSON库识别，也不能被浏览器识别，所以fastjson提供了关闭循环引用检测的功能。
            SerializerFeature.DisableCircularReferenceDetect,
            SerializerFeature.QuoteFieldNames,//输出key时是否使用双引号,默认为true
            SerializerFeature.WriteMapNullValue,//是否输出值为null的字段,默认为false
            SerializerFeature.SortField,
            SerializerFeature.MapSortField,
            SerializerFeature.WriteBigDecimalAsPlain,

            //屏蔽掉, 开启后则 SerializerFeature.WriteClassName 不生效，redis序列化会有问题 @see MyGenericFastjsonRedisSerializer
            //SerializerFeature.NotWriteRootClassName,

            //支持数据输出xss漏洞安全转义,不全局打开,建议每个业务按需设置
            //SerializerFeature.BrowserSecure,
    };

    private static FastJsonConfig fastJsonConfig;

    //******************************* toJson or writeJson ***********************************//

    /**
     * 对象序列化
     *
     * @param obj 需要序列化的对象
     * @return 返回json字符串
     */
    public static String toJson(Object obj) {
        return toJson(obj, getFastJsonConfig().getSerializeConfig(), getFastJsonConfig().getSerializeFilters(),
                getFastJsonConfig().getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, getFastJsonConfig().getSerializerFeatures());
    }


    /**
     * 格式化输出json
     *
     * @param object
     * @param prettyFormat 是否格式化
     * @return
     */
    public static String toJson(Object object, boolean prettyFormat) {
        if (!prettyFormat) {
            return toJson(object);
        }
        return toJson(object, SerializerFeature.PrettyFormat);
    }

    public static String toJson(Object object, SerializerFeature... features) {
        return toJson(object, JSON.DEFAULT_GENERATE_FEATURE, features);
    }

    /**
     * @param object
     * @param dateFormat
     * @param features
     * @return
     */
    public static String toJsonWithDateFormat(Object object, String dateFormat,
                                              SerializerFeature... features) {
        return toJson(object, getFastJsonConfig().getSerializeConfig(), getFastJsonConfig().getSerializeFilters(), dateFormat, JSON.DEFAULT_GENERATE_FEATURE, features);
    }

    public static String toJson(Object object, int defaultFeatures, SerializerFeature... features) {

        return JSON.toJSONString(object, defaultFeatures, features);

    }

    public static String toJson(Object object, SerializeFilter[] filters, SerializerFeature... features) {
        return toJson(object, getFastJsonConfig().getSerializeConfig(), filters, getFastJsonConfig().getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, features);
    }

    public static String toJsonWithDateFormat(Object object, String dateFormat, SerializeFilter[] filters, SerializerFeature... features) {
        return toJson(object, getFastJsonConfig().getSerializeConfig(), filters, dateFormat, JSON.DEFAULT_GENERATE_FEATURE, features);
    }

    public static String toJson(Object object, SerializeConfig config, SerializerFeature... features) {
        return toJson(object, config, getFastJsonConfig().getSerializeFilters(), features);
    }

    public static String toJsonWithDateFormat(Object object, String dateFormat, SerializeConfig config, SerializerFeature... features) {
        return toJsonWithDateFormat(object, dateFormat, config, getFastJsonConfig().getSerializeFilters(), features);
    }

    public static String toJson(Object object, //
                                SerializeConfig config, //
                                SerializeFilter filter, //
                                SerializerFeature... features) {
        return toJson(object, config, new SerializeFilter[]{filter}, getFastJsonConfig().getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, features);
    }

    public static String toJson(Object object, //
                                SerializeConfig config, //
                                SerializeFilter[] filters, //
                                SerializerFeature... features) {
        return toJson(object, config, filters, getFastJsonConfig().getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, features);
    }

    public static String toJsonWithDateFormat(Object object, //
                                              String dateFormat,
                                              SerializeConfig config, //
                                              SerializeFilter[] filters, //
                                              SerializerFeature... features) {
        return toJson(object, config, filters, dateFormat, JSON.DEFAULT_GENERATE_FEATURE, features);
    }

    public static String toJson(Object object, //
                                SerializeConfig config, //
                                SerializeFilter[] filters, //
                                String dateFormat, //
                                int defaultFeatures, //
                                SerializerFeature... features) {
        return JSON.toJSONString(object, config, filters, dateFormat, defaultFeatures, features);
    }

    public static byte[] toJsonBytes(Object object, SerializerFeature... features) {
        return toJsonBytes(object, JSON.DEFAULT_GENERATE_FEATURE, features);
    }

    /**
     * @since 1.2.11
     */
    public static byte[] toJsonBytes(Object object, int defaultFeatures, SerializerFeature... features) {
        return toJsonBytes(object, getFastJsonConfig().getSerializeConfig(), defaultFeatures, features);
    }

    public static byte[] toJsonBytes(Object object, SerializeConfig config, SerializerFeature... features) {
        return toJsonBytes(object, config, JSON.DEFAULT_GENERATE_FEATURE, features);
    }

    /**
     * @since 1.2.11
     */
    public static byte[] toJsonBytes(Object object, SerializeConfig config, int defaultFeatures, SerializerFeature... features) {
        return JSON.toJSONBytes(object, config, defaultFeatures, features);
    }


    public static void writeJson(Writer writer, Object object, SerializerFeature... features) {
        writeJson(writer, object, JSON.DEFAULT_GENERATE_FEATURE, features);
    }

    /**
     * @since 1.2.11
     */
    public static void writeJson(Writer writer, Object object, int defaultFeatures, SerializerFeature... features) {
        JSON.writeJSONString(writer, object, defaultFeatures, features);
    }

    /**
     * write object as json to OutputStream
     *
     * @param os       output stream
     * @param object
     * @param features serializer features
     * @throws IOException
     * @since 1.2.11
     */
    public static final int writeJson(OutputStream os, //
                                      Object object, //
                                      SerializerFeature... features) {
        return writeJson(os, object, JSON.DEFAULT_GENERATE_FEATURE, features);
    }

    /**
     * @since 1.2.11
     */
    public static final int writeJson(OutputStream os, //
                                      Object object, //
                                      int defaultFeatures, //
                                      SerializerFeature... features) {
        return writeJson(os,  //
                IOUtils.UTF8, //
                object, //
                getFastJsonConfig().getSerializeConfig(), //
                getFastJsonConfig().getSerializeFilters(), //
                getFastJsonConfig().getDateFormat(), //
                defaultFeatures, //
                features);
    }

    public static final int writeJsonWithDateFormat(OutputStream os, //
                                                    Object object, //
                                                    String dataFormat, //
                                                    SerializerFeature... features) throws IOException {
        return writeJson(os, //
                IOUtils.UTF8, //
                object, //
                getFastJsonConfig().getSerializeConfig(), //
                getFastJsonConfig().getSerializeFilters(), //
                dataFormat, //
                JSON.DEFAULT_GENERATE_FEATURE, //
                features);
    }

    public static final int writeJson(OutputStream os, //
                                      Charset charset, //
                                      Object object, //
                                      SerializeConfig config, //
                                      SerializeFilter[] filters, //
                                      String dateFormat, //
                                      int defaultFeatures, //
                                      SerializerFeature... features) {
        try {
            return JSON.writeJSONString(os, charset, object, config, filters, dateFormat, defaultFeatures, features);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    //************************ parse **********************************//
    public static <T> T parseObject(String json, Class<T> cls) {
        return JSON.parseObject(json, cls);
    }

    public static Object parse(String text) {
        return parse(text, JSON.DEFAULT_PARSER_FEATURE);
    }

    public static Object parse(String text, int features) {

        return JSON.parse(text, features);

    }

    public static Object parse(String text, Feature... features) {

        return JSON.parse(text, features);

    }

    public static Object parse(byte[] input, Feature... features) {

        return JSON.parse(input, features);

    }


    //************************ parse JSONObject **********************************//


    public static JSONObject parseObject(String text, Feature... features) {
        return (JSONObject) parse(text, features);
    }

    public static JSONObject parseObject(String text) {
        Object obj = parse(text);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }


        return (JSONObject) JSON.toJSON(obj);

    }

    public static <T> T parseObject(String text, TypeReference<T> type, Feature... features) {

        return JSON.parseObject(text, type, features);

    }

    public static <T> T parseObject(String json, Class<T> clazz, Feature... features) {

        return JSON.parseObject(json, clazz, features);

    }

    public static <T> T parseObject(String json, Type type, Feature... features) {

        return JSON.parseObject(json, type, features);

    }

    public static <T> T parseObject(String input, Type clazz, int featureValues, Feature... features) {

        return JSON.parseObject(input, clazz, featureValues, features);

    }

    public static <T> T parseObject(String input, Type clazz, ParserConfig config, Feature... features) {

        return JSON.parseObject(input, clazz, config, features);

    }

    public static <T> T parseObject(String input, Type clazz, ParserConfig config) {

        return JSON.parseObject(input, clazz, config, getFastJsonConfig().getFeatures());

    }

    public static <T> T parseObject(byte[] bytes, Type clazz, Feature... features) {

        return JSON.parseObject(bytes, clazz, features);

    }

    /**
     * @param json
     * @param keyType
     * @param valueType
     * @param <K>
     * @param <V>
     * @return
     * @see {https://github.com/alibaba/fastjson/wiki/TypeReference}
     * <p>
     * 例如：// 可以这样使用 String json = "{1:{name:\"ddd\"},2:{name:\"zzz\"}}";
     */
    public static <K, V> Map<K, V> parseToMap(String json,
                                              Class<K> keyType,
                                              Class<V> valueType) {
        return parseObject(json,
                new TypeReference<Map<K, V>>(keyType, valueType) {
                });
    }


    //************************ parse Object **********************************//


    public static <T> T fromJson(String text, Class<T> clazz) {

        return JSON.parseObject(text, clazz);

    }

    /**
     * <pre>
     * String jsonStr = "[{\"id\":1001,\"name\":\"Jobs\"}]";
     * List&lt;Model&gt; models = JSON.parseObject(jsonStr, new TypeReference&lt;List&lt;Model&gt;&gt;() {});
     * </pre>
     *
     * @param text     json string
     * @param type     type refernce
     * @param features
     * @return
     */
    public static <T> T fromJson(String text, TypeReference<T> type, Feature... features) {

        return JSON.parseObject(text, type, features);

    }

    public static <T> T fromJson(String json, Class<T> clazz, Feature... features) {

        return JSON.parseObject(json, clazz, features);

    }

    public static <T> T fromJson(String text, Class<T> clazz, ParseProcess processor, Feature... features) {

        return JSON.parseObject(text, clazz, processor, features);

    }

    public static <T> T fromJson(String json, Type type, Feature... features) {

        return JSON.parseObject(json, type, features);

    }

    public static <T> T fromJson(String input, Type clazz, ParseProcess processor, Feature... features) {

        return JSON.parseObject(input, clazz, processor, features);

    }

    public static <T> T fromJson(String input, Type clazz, int featureValues, Feature... features) {

        return JSON.parseObject(input, clazz, featureValues, features);

    }

    /**
     * @since 1.2.11
     */
    public static <T> T fromJson(String input, Type clazz, ParserConfig config, Feature... features) {

        return JSON.parseObject(input, clazz, config, features);

    }

    public static <T> T fromJson(String input, Type clazz, ParserConfig config, int featureValues,
                                 Feature... features) {

        return JSON.parseObject(input, clazz, config, featureValues, features);

    }


    public static <T> T fromJson(byte[] bytes, Type clazz, Feature... features) {

        return JSON.parseObject(bytes, clazz, features);

    }

    /**
     * @since 1.2.11
     */
    public static <T> T fromJson(InputStream is, //
                                 Type type, //
                                 Feature... features) {

        try {
            return JSON.parseObject(is, type, features);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    //************************ parse Array **********************************//

    public static JSONArray parseArray(String text) {

        return JSON.parseArray(text);

    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {

        return JSON.parseArray(text, clazz);

    }

    public static List<Object> parseArray(String text, Type[] types) {

        return JSON.parseArray(text, types);

    }

    public static FastJsonConfig buildDefaultFastJsonConfig() {
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(defaultFeatures);
        config.setCharset(StandardCharsets.UTF_8);
        config.setDateFormat("yyyy-MM-dd hh:mm:ss");
        return config;
    }

    public static FastJsonConfig getFastJsonConfig() {
        if (fastJsonConfig == null) {
            synchronized (FastJsonUtils.class) {
                if (fastJsonConfig == null) {
                    fastJsonConfig = buildDefaultFastJsonConfig();
                }
            }
        }
        return fastJsonConfig;
    }

}
