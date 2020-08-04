package ace.fw.logic.common.aop.Interceptor.handler.annotations;

import ace.fw.logic.common.aop.Interceptor.handler.ThrowableHandler;
import ace.fw.logic.common.aop.Interceptor.handler.impl.ThrowableHandlerImpl;

import java.lang.annotation.*;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/3 10:28
 * @description 自动为方法添加默认异常处理切面
 * 默认异常处理器 {@link ThrowableHandlerImpl}
 * 可通过 {@link ThrowableHandlerAspect#throwableHandlerClass()}更改异常处理器
 * 或者全局注册 {@link ThrowableHandler} 接口,覆盖默认的异常处理器
 */
@Documented
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ThrowableHandlerAspect {
    /**
     * 异常处理器
     *
     * @return
     */
    Class<? extends ThrowableHandler> throwableHandlerClass() default ThrowableHandlerImpl.class;
}