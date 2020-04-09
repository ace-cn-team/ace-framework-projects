package ace.fw.mq.rocketmq.constants;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 1:27
 * @description
 */
public interface RocketMQConstant {
    /**
     * rocketMq服务端限制最大消息体大小,最大4M，包含消息头部1KB
     * 4M 防盗门要要排除头大小(1KB)
     */
    Integer MESSAGE_BODY_MAX_BYTES = 4 * 1024 * 1024 - 1024;
    /**
     * 默认消息体大小 2M
     */
    Integer DEFAULT_MESSAGE_BODY_MAX_BYTES = 2 * 1024 * 1024;
    /**
     * 消息体超过默认大小（1M)，记录警告日志
     */
    Integer DEFAULT_WARNING_MESSAGE_BODY_MAX_BYTES = 1 * 1024 * 1024;
}
