package ace.fw.mq.rocketmq.impl.producer;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/10 12:08
 * @description
 */
public class TransactionCheckListenerImpl implements TransactionCheckListener {
    @Override
    public LocalTransactionState checkLocalTransactionState(MessageExt msg) {

        return null;
    }
}
