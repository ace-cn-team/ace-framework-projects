package ace.fw.mq.rocketmq.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Objects;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/13 18:12
 * @description
 */
@Slf4j
public final class LogUtils {
    public static String getLogFromMessage(MessageExt message) {
        if (Objects.isNull(message)) {
            return StringUtils.EMPTY;
        }
        String errorMsg = String.format("[msgId:%s],[topic:%s],[tags:%s],[keys:%s]",
                message.getMsgId(),
                message.getTopic(),
                message.getTags(),
                message.getKeys()
        );
        return errorMsg;
    }

    public static String getLogFromMessage(Message message) {
        if (Objects.isNull(message)) {
            return StringUtils.EMPTY;
        }
        String errorMsg = String.format("[topic:%s],[tags:%s],[keys:%s]",
                message.getTopic(),
                message.getTags(),
                message.getKeys()
        );
        return errorMsg;
    }

    public static String toJson(Object object) {
        if (Objects.isNull(object)) {
            return StringUtils.EMPTY;
        }
        try {
            return JSON.toJSONString(object);
        } catch (Exception ex) {
            log.error("无法序列化为json", ex);
        }
        return StringUtils.EMPTY;
    }
}
