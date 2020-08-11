package ace.fw.json.jackson;

import ace.fw.json.jackson.model.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/16 18:40
 * @description jackson 工厂
 */

@Slf4j
public class ObjectMapperFactory {

    private static ObjectMapper defaultObjectMapper;
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取默认 {@link ObjectMapper} 对象
     *
     * @return
     */
    public static ObjectMapper getDefaultObjectMapper() {
        if (defaultObjectMapper != null) {
            return defaultObjectMapper;
        }
        synchronized (ObjectMapperFactory.class) {
            if (defaultObjectMapper != null) {
                return defaultObjectMapper;
            }
            defaultObjectMapper = new ObjectMapper();

            defaultObjectMapper = initObjectMapper(defaultObjectMapper);
            return defaultObjectMapper;
        }
    }

    private static ObjectMapper initObjectMapper(ObjectMapper objectMapper) {
        objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        objectMapper.registerModules(javaTimeModule);
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        // WRAP_ROOT_VALUE(false) : 序列化的json是否显示根节点
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        // INDENT_OUTPUT(false): 允许或禁止是否以缩进的方式展示json
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        // 当类的一个属性外部无法访问(如：没有getter setter 的私有属性)，
        // 且没有annotation 标明需要序列化时，如果FAIL_ON_EMPTY_BEANS 是true 将会跑出异常，如果是false 则不会跑出异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 如果POJO 中有一个直接自我引用，在序列化的时候会抛出 com.fasterxml.jackson.databind.JsonMappingException
        objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, true);
        // 如果序列化过程中，如果抛出 Exception 将会被包装，添加额外的上下文信息
        objectMapper.configure(SerializationFeature.WRAP_EXCEPTIONS, true);
        // value序列化日期以timestamps输出
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // key序列化日期以timestamps输出
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        // 序列化日期是否带上时区ID
        objectMapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
        // 序列化char[]时以json数组输出
        objectMapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, false);
        // 序列化map时对key进行排序操作
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

        //  反序列化时,遇到未知属性(那些没有对应的属性来映射的属性,
        //  并且没有任何setter或handler来处理这样的属性)时是否引起结果失败。
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 反序列化时,遇到null属性映射在java基本数据类型（int或douuble）是否报异常。
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        // 反序列化时,遇到integer numbers属性映射在enum类型时，如果为true,numbers将不可以映射到enum中。
        objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
        // 反序列化时，遇到类名错误或者map中id找不到时是否报异常。
        objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, true);
        // 反序列化时，遇到json数据存在两个相同的key时是否报异常。
        objectMapper.configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, false);
        // 反序列化时，遇到json属性字段为可忽略的是否报异常。
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        return objectMapper;
    }


    public static void main(String[] args) throws JsonProcessingException {
        Test test = Test.builder().integer(1)
                .now(LocalDateTime.now())
                .string("11")
                .aDouble(null)
                .build();

        ObjectMapper mapper = getDefaultObjectMapper();
        String json = mapper.writeValueAsString(test);
        System.out.println(json);

        Test test1 = mapper.readValue(json, Test.class);
        System.out.println(test1.toString());
    }
}
