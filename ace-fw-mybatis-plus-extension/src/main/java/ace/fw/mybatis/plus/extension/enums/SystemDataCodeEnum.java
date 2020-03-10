package ace.fw.mybatis.plus.extension.enums;

import ace.fw.enums.BaseEnum;
import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/3 11:57
 * @description
 */
public enum SystemDataCodeEnum implements BaseEnum<String> {
    ServiceNotExist("50001", "service接口不存在");
    @Getter
    private String code;
    @Getter
    private String desc;

    SystemDataCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
