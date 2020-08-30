package ace.fw.restful.base.api.enums;

import ace.fw.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/27 10:43
 * @description 逻辑运算符号
 */

@AllArgsConstructor
public enum LogicalOpEnum implements BaseEnum<String> {
    AND("and", "and"),
    OR("or", "or");;
    @Getter
    private String code;
    @Getter
    private String desc;
}
