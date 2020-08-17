package ace.fw.util;

import ace.fw.enums.BaseEnum;
import ace.fw.exception.BusinessException;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/3/17 15:13
 * @description
 */
public final class BusinessErrorUtils {
    public static void throwNew(BaseEnum<String> baseEnum) {
        throw new BusinessException(baseEnum.getCode(), baseEnum.getDesc());
    }

    public static void throwNew(BaseEnum<String> baseEnum, Throwable throwable) {
        throw create(baseEnum, throwable);
    }

    public static void throwNew(String message) {
        throw new BusinessException(message);
    }

    public static BusinessException create(BaseEnum<String> baseEnum, Throwable ex) {
        return new BusinessException(baseEnum.getCode(), baseEnum.getDesc(), ex);
    }
}
