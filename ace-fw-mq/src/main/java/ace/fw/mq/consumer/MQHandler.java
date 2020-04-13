package ace.fw.mq.consumer;

import ace.fw.mq.enums.MQResponseEnum;
import ace.fw.mq.model.MessageContext;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/11 10:29
 * @description 普通消息消费者，消费接口
 */
public interface MQHandler<MessageBody> {
    /**
     * 消息消费方法
     *
     * @param messageContext 消息消费的上下文
     * @return {@link MQResponseEnum}
     */
    MQResponseEnum consume(MessageContext<MessageBody> messageContext);
}
