package ace.fw.logic.common.aop.Interceptor.handler.impl;

import ace.fw.enums.SystemCodeEnum;
import ace.fw.exception.BusinessException;
import ace.fw.exception.SystemException;
import ace.fw.logic.common.aop.Interceptor.handler.ThrowableHandler;
import ace.fw.model.response.GenericResponseExt;
import ace.fw.util.GenericResponseExtUtils;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.validation.BindException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/4 10:52
 * @description 异常处理接口
 */
@Slf4j
public class ThrowableHandlerImpl implements ThrowableHandler<GenericResponseExt> {

    @Override
    public GenericResponseExt handle(MethodInvocation methodInvocation, Throwable throwable) {
        return handle(throwable);
    }


    /**
     * 异常统一处理
     *
     * @param ex
     * @return 返回null值，系统继续搜索其它处理程序handler,返回GenericResponse，则系统不继续搜索其它handler，直接返回给上层
     */
    public GenericResponseExt handle(Throwable ex) {
        if (ex instanceof BusinessException) {
            return handleBusinessException((BusinessException) ex);
        }
        if (ex instanceof ConstraintViolationException) {
            return handleConstraintViolationException((ConstraintViolationException) ex);
        }
        if (ex instanceof SystemException) {
            return handleSystemException((SystemException) ex);
        }
        if (ex instanceof BindException) {
            return handleBindException((BindException) ex);
        }
        if (ex.getClass().equals(RuntimeException.class)) {
            return handlerRuntimeException((RuntimeException) ex);
        }
        return handlerAllException(ex);

    }

    private GenericResponseExt handleConstraintViolationException(ConstraintViolationException ex) {
        ConstraintViolation constraintViolation = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .orElse(null);
        return GenericResponseExtUtils
                .builder()
                .code(SystemCodeEnum.ERROR_CHECK_PARAMETER.getCode())
                .message(constraintViolation.getMessage())
                .build();
    }

    private GenericResponseExt handleBusinessException(BusinessException ex) {
        return GenericResponseExtUtils
                .builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
    }

    private GenericResponseExt handlerAllException(Throwable ex) {
        log.error("handle exception", ex);
        return GenericResponseExtUtils.buildBySystemCodeEnum(SystemCodeEnum.ERROR_SYSTEM_EXCEPTION);
    }

    private GenericResponseExt handlerRuntimeException(RuntimeException ex) {
        log.error("handle runtime exception", ex);
        return GenericResponseExtUtils.builder()
                .code(SystemCodeEnum.ERROR_SYSTEM_EXCEPTION.getCode())
                .message(ex.getMessage())
                .build();
    }


    private GenericResponseExt handleSystemException(SystemException ex) {
        log.error("handle system exception", ex);
        return GenericResponseExtUtils.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
    }

    /**
     * 参数验证、绑定异常
     *
     * @param ex
     * @return
     */

    private GenericResponseExt handleBindException(BindException ex) {
        GenericResponseExt response = new GenericResponseExt();
        if (Objects.nonNull(ex.getBindingResult().getFieldError()) && !ex.getBindingResult().getFieldError().isBindingFailure()) {
            response.setCode(SystemCodeEnum.ERROR_CHECK_PARAMETER.getCode());
            response.setMessage(ex.getBindingResult().getFieldError().getDefaultMessage());
        } else {
            response.setCode(SystemCodeEnum.ERROR_INVALID_PARAMETER.getCode());
            response.setMessage(SystemCodeEnum.ERROR_INVALID_PARAMETER.getDesc());
        }
        return response;
    }

}
