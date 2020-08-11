package ace.fw.mq.rocketmq.impl.serializer;

import ace.fw.json.JsonPlugin;
import ace.fw.mq.serializer.Deserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/10 15:05
 * @description
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class JsonDeserializerImpl implements Deserializer {
    private JsonPlugin jsonPlugin;

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> cls) {
        try {
            String json = new String(bytes, "utf-8");
            return jsonPlugin.toObject(json, cls);
        } catch (Exception e) {
            throw new RuntimeException("json反序列化失败");
        }
    }
}
