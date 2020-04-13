package ace.fw.mq.rocketmq.enums;

import ace.fw.enums.BaseEnum;
import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/11 14:22
 * @description
 */
public enum MQHandlerTypeEnum implements BaseEnum<Integer> {
    CONCURRENTLY(0, "并发消费"),

    ORDERLY(1, "顺序消费"),
    ;

    @Getter
    private Integer code;
    @Getter
    private String desc;

    MQHandlerTypeEnum(Integer code, String desc) {

        this.code = code;
        this.desc = desc;
    }
}