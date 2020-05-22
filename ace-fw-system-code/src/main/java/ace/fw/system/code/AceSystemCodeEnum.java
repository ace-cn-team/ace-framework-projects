package ace.fw.system.code;

import ace.fw.enums.BaseEnum;
import ace.fw.enums.SystemCodeEnum;
import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/16 15:03
 * @description 系统代码枚举
 */
public enum AceSystemCodeEnum implements BaseEnum<String> {
    SUCCESS(SystemCodeEnum.SUCCESS),
    BUSINESS_EXCEPTION(SystemCodeEnum.BUSINESS_EXCEPTION),
    ERROR_CHECK_PARAMETER(SystemCodeEnum.ERROR_CHECK_PARAMETER),
    ERROR_INVALID_PARAMETER(SystemCodeEnum.ERROR_INVALID_PARAMETER),
    ERROR_CLIENT_ABORT_EXCEPTION(SystemCodeEnum.ERROR_CLIENT_ABORT_EXCEPTION),
    ERROR_SYSTEM_EXCEPTION(SystemCodeEnum.ERROR_SYSTEM_EXCEPTION),
    ERROR_HTTP_403_EXCEPTION(SystemCodeEnum.ERROR_HTTP_403_EXCEPTION),
    ERROR_HTTP_404_EXCEPTION(SystemCodeEnum.ERROR_HTTP_404_EXCEPTION),

    MQ_EXCEPTION("20001", "MQ消息发送失败"),
    MQ_MESSAGE_BODY_LIMIT_ERROR("20002", "MQ消息发送失败,消息体超出限制大小"),
    MQ_CUSTOM_MESSAGE_BODY_LIMIT_ERROR("20003", "MQ消息发送失败,消息体超出自定义限制大小"),
    ;
    @Getter
    private String code;
    @Getter
    private String desc;

    AceSystemCodeEnum(String code, String desc) {

        this.code = code;
        this.desc = desc;
    }

    AceSystemCodeEnum(SystemCodeEnum systemCodeEnum) {
        this.code = systemCodeEnum.getCode();
        this.desc = systemCodeEnum.getDesc();
    }
}
