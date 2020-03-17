package ace.fw.enums;

import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/16 15:03
 * @description 系统代码枚举
 */
public enum SystemCodeEnum implements BaseEnum<String> {
    SUCCESS("0", "成功"),
    BUSINESS_EXCEPTION("10001", "业务异常"),
    ERROR_CHECK_PARAMETER("10100", "参数校验失败"),
    ERROR_INVALID_PARAMETER("10110", "请求参数无效"),
    ERROR_CLIENT_ABORT_EXCEPTION("10101", "客户端已关闭"),
    ERROR_SYSTEM_EXCEPTION("10000", "系统异常"),
    ERROR_HTTP_403_EXCEPTION("11403", "服务器禁访问"),
    ERROR_HTTP_404_EXCEPTION("11404", "接口不存在"),
    ;
    @Getter
    private String code;
    @Getter
    private String desc;

    SystemCodeEnum(String code, String desc) {

        this.code = code;
        this.desc = desc;
    }

}
