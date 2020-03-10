package ace.fw.mq.enums;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/2 19:37
 * @description
 */
public enum MqResponseEnum {
    /**
     * 消费成功，继续消费下一条消息
     */
    CommitMessage,
    /**
     * 消费失败，告知服务器稍后再投递这条消息，继续消费其他消息
     */
    ReconsumeLater,
}