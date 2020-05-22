package ace.fw.mq.enums;

import ace.fw.enums.BaseEnum;
import ace.fw.system.code.AceSystemCodeEnum;
import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/16 15:03
 * @description 系统代码枚举
 */
public enum MqErrorEnum implements BaseEnum<String> {
    MQ_EXCEPTION(AceSystemCodeEnum.MQ_EXCEPTION),
    MESSAGE_BODY_LIMIT_ERROR(AceSystemCodeEnum.MQ_MESSAGE_BODY_LIMIT_ERROR),
    CUSTOM_MESSAGE_BODY_LIMIT_ERROR(AceSystemCodeEnum.MQ_CUSTOM_MESSAGE_BODY_LIMIT_ERROR),
    ;
    @Getter
    private String code;
    @Getter
    private String desc;

    MqErrorEnum(String code, String desc) {

        this.code = code;
        this.desc = desc;
    }

    MqErrorEnum(BaseEnum<String> baseEnum) {
        this.code = baseEnum.getCode();
        this.desc = baseEnum.getDesc();
    }
}
