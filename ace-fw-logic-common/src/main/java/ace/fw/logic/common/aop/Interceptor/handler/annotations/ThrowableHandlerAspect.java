package ace.fw.logic.common.aop.Interceptor.handler.annotations;

import ace.fw.logic.common.aop.Interceptor.handler.ThrowableHandler;
import ace.fw.logic.common.aop.Interceptor.handler.impl.ThrowableHandlerImpl;

import java.lang.annotation.*;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/3 10:28
 * @description
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