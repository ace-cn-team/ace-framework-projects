package ace.fw.mq.producer;

import ace.fw.mq.enums.MqResponseEnum;
import ace.fw.mq.enums.TransactionStatusEnum;
import ace.fw.mq.model.CallbackContext;
import ace.fw.mq.model.Topic;
import org.apache.commons.collections.ListUtils;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/2 1:06
 * @description 事务MQ Producer
 */
public interface TransactionMqCheckListener<MessageBody> {
    /**
     * 事务MQ回查
     *
     * @param callbackContext
     * @return 事务信息 操作结果
     */
    MqResponseEnum check(CallbackContext<MessageBody> callbackContext);

    /**
     * 监听的Topic
     *
     * @return
     */
    Topic getTopic();

    /**
     * 监听topic的tags
     *
     * @return null或者空 代表监听topic下所有的tags
     */
    default List<String> getTags() {
        return ListUtils.EMPTY_LIST;
    }
}
