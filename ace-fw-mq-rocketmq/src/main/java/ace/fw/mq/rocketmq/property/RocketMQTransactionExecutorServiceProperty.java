package ace.fw.mq.rocketmq.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.concurrent.*;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/13 16:48
 * @description 事务MQ回查机制，线程池配置.参考 {@link java.util.concurrent.ThreadPoolExecutor}
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RocketMQTransactionExecutorServiceProperty {
    /**
     * 线程池保留的核心线程数量
     */
    private Integer corePoolSize = 3;
    /**
     * 线程池最大的线程数量
     */
    private Integer maximumPoolSize = 6;
    /**
     * 超时时间
     */
    private Long keepAliveTime = 30L;
    /**
     * 超时时间单位
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    /**
     * 回查队列最大长度
     */
    private Integer queueLength = 200;
}
