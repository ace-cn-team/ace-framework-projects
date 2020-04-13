package ace.fw.mq.rocketmq.impl.producer;

import ace.fw.model.response.GenericResponseExt;
import ace.fw.mq.enums.MqErrorEnum;
import ace.fw.mq.enums.TransactionStatusEnum;
import ace.fw.mq.model.TransactionMessage;
import ace.fw.mq.producer.TransactionMqProducer;
import ace.fw.mq.serializer.Serializer;
import ace.fw.util.GenericResponseExtUtils;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 10:30
 * @description
 */

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class TransactionMQProducerImpl
        extends AbstractMQProducer
        implements TransactionMqProducer {

    private TransactionMQProducer transactionMQProducer;


    protected TransactionMQProducer getTransactionMQProducer() {
        if (Objects.isNull(transactionMQProducer)) {
            synchronized (TransactionMQProducerImpl.this) {
                transactionMQProducer = (TransactionMQProducer) this.getMqProducer();
            }
        }
        return transactionMQProducer;
    }

    @Override
    public GenericResponseExt<TransactionStatusEnum> send(TransactionMessage message) {
        return this.send(message, this.getDefaultSerializer(), null);
    }

    @Override
    public GenericResponseExt<TransactionStatusEnum> send(TransactionMessage message, Serializer serializer) {
        return this.send(message, serializer, null);
    }


    public GenericResponseExt<TransactionStatusEnum> send(TransactionMessage message, Serializer serializer, Object args) {

        val rocketMQMessage = this.getMessageConverter().toRocketMQMessage(message, serializer);

        GenericResponseExt<Boolean> checkRocketMQMessageResult = this.getRocketMQMessageChecker().check(rocketMQMessage);
        if (checkRocketMQMessageResult.getSuccess() == false) {
            GenericResponseExt<TransactionStatusEnum> result = new GenericResponseExt<TransactionStatusEnum>();
            result.setData(TransactionStatusEnum.UNKNOWN);
            result.setCode(checkRocketMQMessageResult.getCode());
            result.setMessage(checkRocketMQMessageResult.getMessage());
            return result;
        }

        try {
            TransactionSendResult sendResult = this.getTransactionMQProducer()
                    .sendMessageInTransaction(rocketMQMessage, new LocalTransactionExecuter() {
                        @Override
                        public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
                            LocalTransactionState result = LocalTransactionState.UNKNOW;
                            try {
                                TransactionStatusEnum transactionStatusResult = message.getTransactionEvent().get();
                                result = TransactionStatusConverter.toRocketMQTransactionState(transactionStatusResult);
                            } catch (Exception ex) {
                                log.error("事务消息的事务事件执行失败", ex);
                            }
                            return result;
                        }
                    }, args);
            if (SendStatus.SEND_OK.equals(sendResult.getSendStatus()) == false) {
                GenericResponseExt<TransactionStatusEnum> result = new GenericResponseExt<TransactionStatusEnum>();
                result.setData(TransactionStatusEnum.UNKNOWN);
                result.setCode(MqErrorEnum.MQ_EXCEPTION.getCode());
                result.setMessage(MqErrorEnum.MQ_EXCEPTION.getDesc());
                return result;
            }
            val transactionStatusResult = TransactionStatusConverter.toTransactionStatus(sendResult.getLocalTransactionState());
            return GenericResponseExtUtils.buildSuccessWithData(transactionStatusResult);
        } catch (Exception ex) {
            List<String> tags = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(message.getTags())) {
                tags.addAll(message.getTags());
            }
            String msg = String.format("[事务消息发送失败],[topic:%s-%s-%s]",
                    message.getTopic().getCode(),
                    message.getTopic().getDesc(),
                    String.join(",", tags));
            log.error(msg, ex);
        }
        return GenericResponseExtUtils.buildWithDataAndCodeEnum(TransactionStatusEnum.UNKNOWN, MqErrorEnum.MQ_EXCEPTION);
    }

}
