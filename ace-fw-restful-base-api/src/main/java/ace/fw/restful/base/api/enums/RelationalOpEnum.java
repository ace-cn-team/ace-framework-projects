package ace.fw.restful.base.api.enums;

import ace.fw.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/27 10:43
 * @description 逻辑操作符号
 */

@AllArgsConstructor
public enum RelationalOpEnum implements BaseEnum<String> {
    EQ("eq", "=="),
    NE("ne", "!="),

    IN("in ", "in"),
    LIKE("like ", "like"),
    LIKE_LEFT("like_left ", "like-left"),
    LIKE_RIGHT("like_right ", "like-right"),
    GT("gt ", "gt"),
    GE("ge ", "ge"),
    LT("lt ", "lt"),
    LE("le ", "le"),
    IS_NULL("is_null", "is-null"),
    IS_NOT_NULL("is_not_null ", "is-not-null"),

    BETWEEN("between ", "between"),
    NOT_BETWEEN("not_between ", "not-between"),
    NOT_IN("not_in ", "not-in"),
    NOT_LIKE("not_like ", "not-like"),
    ;
    @Getter
    private String code;
    @Getter
    private String desc;
}