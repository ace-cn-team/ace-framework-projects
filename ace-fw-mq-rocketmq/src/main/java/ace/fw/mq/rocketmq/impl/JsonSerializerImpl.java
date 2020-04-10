package ace.fw.mq.rocketmq.impl;

import ace.fw.json.JsonPlugin;
import ace.fw.json.fastjson.FastJsonPlugin;
import ace.fw.mq.serializer.Serializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;

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
public class JsonSerializerImpl implements Serializer {
    private JsonPlugin jsonPlugin;

    @Override
    public byte[] serialize(Object object) {
        try {
            return jsonPlugin.toJson(object).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("序列化json失败");
            throw new RuntimeException("序列化json失败");
        }
    }
}
