package ace.fw.mq.enums;

import ace.fw.enums.BaseEnum;
import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/10 9:53
 * @description MQ 类型
 */
public enum MQProducerTypeEnum implements BaseEnum<Integer> {
    PRODUCER(0, "普通MQ生产者"),

    TRANSACTION_PRODUCER(1, "事务MQ生产者"),

    ;

    @Getter
    private Integer code;
    @Getter
    private String desc;

    MQProducerTypeEnum(Integer code, String desc) {

        this.code = code;
        this.desc = desc;
    }
}