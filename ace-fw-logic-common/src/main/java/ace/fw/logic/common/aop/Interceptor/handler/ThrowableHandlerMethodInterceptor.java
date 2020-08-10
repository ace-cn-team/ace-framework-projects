package ace.fw.logic.common.aop.Interceptor.handler;

import ace.fw.logic.common.aop.Interceptor.handler.annotations.ThrowableHandlerAspect;
import lombok.extern.slf4j.Slf4j;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/3 10:41
 * @description 默认逻辑层异常处理拦截器
 */
@Slf4j
public class ThrowableHandlerMethodInterceptor implements MethodInterceptor,
        Ordered,
        ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        ThrowableHandler throwableHandler = this.findThrowableHandler(methodInvocation);
        if (throwableHandler == null) {
            return methodInvocation.proceed();
        }
        try {
            Object result = methodInvocation.proceed();
            return result;
        } catch (Throwable ex) {
            return throwableHandler.handle(methodInvocation, ex);
        }
    }

    private ThrowableHandlerAspect findThrowableHandlerAspect(MethodInvocation methodInvocation) {
        ThrowableHandlerAspect throwableHandlerAspect = AnnotationUtils.findAnnotation(methodInvocation.getMethod(), ThrowableHandlerAspect.class);
        if (throwableHandlerAspect != null) {
            return throwableHandlerAspect;
        }
        Class aClass = methodInvocation.getThis().getClass();
        throwableHandlerAspect = AnnotationUtils.findAnnotation(aClass, ThrowableHandlerAspect.class);
        return throwableHandlerAspect;
    }

    private ThrowableHandler findThrowableHandler(MethodInvocation methodInvocation) {
        ThrowableHandlerAspect throwableHandlerAspect = this.findThrowableHandlerAspect(methodInvocation);
        ThrowableHandler throwableHandler = applicationContext.getBean(throwableHandlerAspect.throwableHandlerClass());
        return throwableHandler;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
