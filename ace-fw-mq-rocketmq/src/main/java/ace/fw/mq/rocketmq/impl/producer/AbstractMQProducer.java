package ace.fw.mq.rocketmq.impl.producer;

import ace.fw.mq.serializer.Serializer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/8 17:56
 * @description
 */
@Data
@Slf4j
public abstract class AbstractMQProducer implements InitializingBean, DisposableBean {

    private MQProducer mqProducer;

    private Serializer defaultSerializer;

    private RocketMQMessageChecker rocketMQMessageChecker;

    private MessageConverter messageConverter;

    @Override
    public void destroy() throws Exception {
        if (Objects.nonNull(this.mqProducer)) {
            this.mqProducer.shutdown();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (Objects.nonNull(this.mqProducer)) {
            this.mqProducer.start();
        }
    }
}
