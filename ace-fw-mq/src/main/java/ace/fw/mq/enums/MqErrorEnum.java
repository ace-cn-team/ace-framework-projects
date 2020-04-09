package ace.fw.mq.enums;

import ace.fw.enums.BaseEnum;
import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/16 15:03
 * @description 系统代码枚举
 */
public enum MqErrorEnum implements BaseEnum<String> {
    MQ_EXCEPTION("10001", "MQ消息发送失败"),
    MESSAGE_BODY_LIMIT_ERROR("10002", "MQ消息发送失败,消息体超出限制大小"),
    CUSTOM_MESSAGE_BODY_LIMIT_ERROR("10003", "MQ消息发送失败,消息体超出自定义限制大小"),
    ;
    @Getter
    private String code;
    @Getter
    private String desc;

    MqErrorEnum(String code, String desc) {

        this.code = code;
        this.desc = desc;
    }

}
