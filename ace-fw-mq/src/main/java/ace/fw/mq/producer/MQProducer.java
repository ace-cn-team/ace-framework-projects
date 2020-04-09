package ace.fw.mq.producer;

import ace.fw.model.response.GenericResponseExt;
import ace.fw.mq.model.Message;
import ace.fw.mq.serializer.Serializer;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/2 1:06
 * @description 普通MQ Producer
 */
public interface MQProducer {
    /**
     * 同步发送普通MQ
     *
     * @param message 发送的消息
     * @return code字段 0 代表发送成功，0以外代码代表发送失败
     */
    GenericResponseExt<Boolean> send(Message message);

    /**
     * 同步发送普通MQ
     *
     * @param message    发送的消息
     * @param serializer 消息的序列化工具
     * @return code字段 0 代表发送成功，0以外代码代表发送失败
     */
    GenericResponseExt<Boolean> send(Message message, Serializer serializer);
}
