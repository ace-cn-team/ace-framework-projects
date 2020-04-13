package ace.fw.mq.consumer;

import ace.fw.mq.model.Topic;
import ace.fw.mq.serializer.Deserializer;
import org.apache.commons.collections.ListUtils;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/11 10:29
 * @description 普通消息消费者，订阅与消费接口
 */
public interface MQListener<MessageBody> extends MQHandler {

    /**
     * 获取订阅的topic
     *
     * @return
     */
    Topic getSubscribeTopic();

    /**
     * 获取订阅topic的tags
     *
     * @return
     */
    default List<String> getSubscribeTags() {
        return ListUtils.EMPTY_LIST;
    }

    /**
     * 获取反序列化工具,返回空，使用默认反序列化工具
     *
     * @return
     */
    default Deserializer getDeserializer() {
        return null;
    }
}
