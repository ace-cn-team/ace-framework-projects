package ace.fw.mq.rocketmq.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/13 18:12
 * @description
 */
@Slf4j
public final class LogUtils {
    public static String getLogFromMessage(MessageExt messageExt) {
        String errorMsg = String.format("[msgId:%s],[topic:%s],[tags:%s],[keys:%s]",
                messageExt.getMsgId(),
                messageExt.getTopic(),
                messageExt.getTags(),
                messageExt.getKeys()
        );
        return errorMsg;
    }

    public static String getLogFromMessage(Message message) {
        String errorMsg = String.format("[topic:%s],[tags:%s],[keys:%s]",
                message.getTopic(),
                message.getTags(),
                message.getKeys()
        );
        return errorMsg;
    }

    public static String toJson(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception ex) {
            log.error("无法序列化为json", ex);
        }
        return StringUtils.EMPTY;
    }
}
