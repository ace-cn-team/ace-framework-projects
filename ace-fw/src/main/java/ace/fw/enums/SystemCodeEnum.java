package ace.fw.enums;

import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/16 15:03
 * @description 系统代码枚举
 */
public enum SystemCodeEnum implements BaseEnum<String> {
    Success("0", "成功"),
    BusinessException("10001", "业务异常"),
    ErrorCheckParameter("10100", "参数校验失败"),
    ErrorInvalidParameter("10110", "请求参数无效"),
    ErrorClientAbortException("10101", "客户端已关闭"),
    ErrorSystemException("10000", "系统异常"),
    ErrorHttp403Exception("11403", "服务器禁访问"),
    ErrorHttp404Exception("11404", "接口不存在"),

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
