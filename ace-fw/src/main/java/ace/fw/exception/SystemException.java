package ace.fw.exception;

import ace.fw.enums.SystemCodeEnum;
import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/17 17:00
 * @description 系统异常
 */

public class SystemException extends RuntimeException {


    @Getter
    protected String code = SystemCodeEnum.ERROR_SYSTEM_EXCEPTION.getCode();

    public SystemException() {
        super();
    }

    public SystemException(Throwable ex) {
        super(ex);
    }

    public SystemException(String message) {
        super(message);
    }
    public SystemException(String message,Throwable ex) {
        super(message,ex);
    }
    public SystemException(String code, String message) {
        super(message);
        this.code = code;
    }
}
