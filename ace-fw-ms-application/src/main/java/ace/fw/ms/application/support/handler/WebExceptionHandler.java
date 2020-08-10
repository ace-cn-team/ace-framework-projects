package ace.fw.ms.application.support.handler;

import ace.fw.exception.BusinessException;
import ace.fw.exception.SystemException;
import ace.fw.model.response.GenericResponse;
import ace.fw.enums.SystemCodeEnum;
import ace.fw.util.GenericResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/10 18:30
 * @description 异常处理含:
 * 1.controller层面异常
 * 2.
 */
@Slf4j
public class WebExceptionHandler {


    /**
     * 异常统一处理
     *
     * @param request
     * @param response
     * @param handlerMethod
     * @param ex
     * @return 返回null值，系统继续搜索其它处理程序handler,返回GenericResponse，则系统不继续搜索其它handler，直接返回给上层
     */
    public GenericResponse execute(HttpServletRequest request, HttpServletResponse response,
                                   @Nullable HandlerMethod handlerMethod, Exception ex) {
        if (ex instanceof BusinessException) {
            return handleBusinessException((BusinessException) ex);
        } else if (ex instanceof SystemException) {
            return handleSystemException((SystemException) ex);
        } else if (ex instanceof BindException) {
            return handleBindException((BindException) ex);
        } else if (ex instanceof MissingServletRequestParameterException) {
            return handleMissingServletRequestParameterException((MissingServletRequestParameterException) ex);
        } else if (ex instanceof ConstraintViolationException) {
            return handleConstraintViolationException((ConstraintViolationException) ex);
        } else if (ex instanceof MethodArgumentNotValidException) {
            return handleValidateException((MethodArgumentNotValidException) ex);
        } else if (ex instanceof ClientAbortException) {
            return handlerClientAbortException((ClientAbortException) ex);
        } else if (ex instanceof HttpRequestMethodNotSupportedException || ex instanceof HttpMediaTypeNotSupportedException) {
            return handlerHttpRequestMethodNotSupportedException((HttpRequestMethodNotSupportedException) ex);
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            return handlerHttpMediaTypeNotSupportedException((HttpMediaTypeNotSupportedException) ex);
        } else if (response.getStatus() == 404) {
            return handler404HttpStatus(request, response, handlerMethod, ex);
        } else if (response.getStatus() == 403) {
            return handler403HttpStatus(request, response, handlerMethod, ex);
        } else if (ex.getClass().equals(RuntimeException.class)) {
            return handlerRuntimeException((RuntimeException) ex);
        } else {
            return handlerAllException(ex);
        }
    }

    private GenericResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return GenericResponseUtils
                .builder()
                .code(SystemCodeEnum.ERROR_CHECK_PARAMETER.getCode())
                .message(ex.getMessage())
                .build();
    }

    private GenericResponse handleConstraintViolationException(ConstraintViolationException ex) {
        ConstraintViolation constraintViolation = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .orElse(null);
        return GenericResponseUtils
                .builder()
                .code(SystemCodeEnum.ERROR_CHECK_PARAMETER.getCode())
                .message(constraintViolation.getMessage())
                .build();
    }

    private GenericResponse handleBusinessException(BusinessException ex) {
        return GenericResponseUtils
                .builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
    }

    /**
     * 获取错误接口uri
     *
     * @param request
     * @return
     */
    private String getErrorRequestUri(HttpServletRequest request) {
        Object value = request.getAttribute("javax.servlet.error.request_uri");
        if (Objects.isNull(value)) {
            return "";
        }
        return (String) value;
    }

    private GenericResponse handler404HttpStatus(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex) {
        GenericResponse genericResponse = GenericResponseUtils.buildBySystemCodeEnum(SystemCodeEnum.ERROR_HTTP_404_EXCEPTION);
        genericResponse.setData(getErrorRequestUri(request));
        return genericResponse;
    }

    private GenericResponse handler403HttpStatus(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex) {
        GenericResponse genericResponse = GenericResponseUtils.buildBySystemCodeEnum(SystemCodeEnum.ERROR_HTTP_403_EXCEPTION);
        genericResponse.setData(getErrorRequestUri(request));
        return genericResponse;
    }

    private GenericResponse handlerAllException(Exception ex) {
        log.error("handler exception", ex);
        return GenericResponseUtils.buildBySystemCodeEnum(SystemCodeEnum.ERROR_SYSTEM_EXCEPTION);
    }

    private GenericResponse handlerRuntimeException(RuntimeException ex) {
        log.error("handler runtime exception", ex);
        return GenericResponseUtils.builder()
                .code(SystemCodeEnum.ERROR_SYSTEM_EXCEPTION.getCode())
                .message(ex.getMessage())
                .build();
    }

    private GenericResponse handlerHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        return GenericResponseUtils.builder()
                .code(SystemCodeEnum.ERROR_SYSTEM_EXCEPTION.getCode())
                .message(ex.getMessage())
                .build();
    }

    private GenericResponse handlerHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return GenericResponseUtils.builder()
                .code(SystemCodeEnum.ERROR_SYSTEM_EXCEPTION.getCode())
                .message(ex.getMessage())
                .build();
    }

    private GenericResponse handlerClientAbortException(ClientAbortException ex) {
        // 客户关掉了浏览器引起异常，简单记录
        log.warn(" System Error ClientAbortException", ex);
        return GenericResponseUtils.buildBySystemCodeEnum(SystemCodeEnum.ERROR_CLIENT_ABORT_EXCEPTION);
    }

    private GenericResponse handleSystemException(SystemException ex) {
        log.error(String.format("code:%s,message:%s", ex.getCode(), ex.getMessage()), ex);
        return GenericResponseUtils.builder()
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

    private GenericResponse handleBindException(BindException ex) {
        GenericResponse response = new GenericResponse();
        if (Objects.nonNull(ex.getBindingResult().getFieldError()) && !ex.getBindingResult().getFieldError().isBindingFailure()) {
            response.setCode(SystemCodeEnum.ERROR_CHECK_PARAMETER.getCode());
            response.setMessage(ex.getBindingResult().getFieldError().getDefaultMessage());
        } else {
            response.setCode(SystemCodeEnum.ERROR_INVALID_PARAMETER.getCode());
            response.setMessage(SystemCodeEnum.ERROR_INVALID_PARAMETER.getDesc());
        }
        return response;
    }

    /**
     * 参数验证、绑定异常
     *
     * @param ex
     * @return
     */
    private GenericResponse handleValidateException(MethodArgumentNotValidException ex) {
        GenericResponse response = new GenericResponse();
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
