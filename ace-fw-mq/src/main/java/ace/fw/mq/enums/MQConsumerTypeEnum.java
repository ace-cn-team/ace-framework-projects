package ace.fw.mq.enums;

import ace.fw.enums.BaseEnum;
import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/10 9:53
 * @description 消费者类型
 */
public enum MQConsumerTypeEnum implements BaseEnum<Integer> {
    CONSUMER(0, "普通MQ消费者"),

    HALF_TRANSACTION_MQ_CHECKER(1, "半事务MQ检查者"),

    ;

    @Getter
    private Integer code;
    @Getter
    private String desc;

    MQConsumerTypeEnum(Integer code, String desc) {

        this.code = code;
        this.desc = desc;
    }
}