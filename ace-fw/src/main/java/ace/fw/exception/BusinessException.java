package ace.fw.exception;

import ace.fw.enums.SystemCodeEnum;
import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/17 17:02
 * @description 业务异常
 */
public class BusinessException extends RuntimeException {

    @Getter
    protected String code = SystemCodeEnum.BusinessException.getCode();

    public BusinessException() {

    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }
}
