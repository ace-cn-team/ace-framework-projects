package ace.fw.mq.producer;

import ace.fw.model.response.GenericResponseExt;
import ace.fw.mq.model.Message;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/2 1:06
 * @description 普通MQ Producer
 */
public interface MqProducer {
    /**
     * 同步发送普通MQ
     *
     * @param message
     * @return code字段 0 代表发送成功，0以外代码代表发送失败
     */
    GenericResponseExt<Boolean> send(Message message);
}
