package ace.fw.logic.common.autoconfigure;

import ace.fw.logic.common.aop.Interceptor.handler.ThrowableHandler;
import ace.fw.logic.common.aop.Interceptor.handler.ThrowableHandlerMethodInterceptor;
import ace.fw.logic.common.aop.Interceptor.handler.annotations.ThrowableHandlerAspect;
import ace.fw.logic.common.aop.Interceptor.handler.impl.ThrowableHandlerImpl;
import ace.fw.logic.common.aop.extension.AceAnnotationMatchingPointcut;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/5 14:38
 * @description
 */
@Configuration
public class ThrowableHandlerAspectAutoConfigure {
    public final static String CONFIG_LOGIC_COMMON_THROWABLE_HANDLER_ASPECT_ENABLE = "ace.fw.logic.common.throwable-handler-aspect.enable";

    @Bean
    @ConditionalOnMissingBean
    public ThrowableHandlerImpl throwableHandler() {
        return new ThrowableHandlerImpl();
    }

    @Bean
    @ConditionalOnProperty(value = CONFIG_LOGIC_COMMON_THROWABLE_HANDLER_ASPECT_ENABLE, matchIfMissing = true, havingValue = "true")
    @ConditionalOnMissingBean
    public ThrowableHandlerMethodInterceptor throwableHandlerMethodInterceptor() {
        ThrowableHandlerMethodInterceptor interceptor = new ThrowableHandlerMethodInterceptor();
        return interceptor;
    }

    @Bean
    @ConditionalOnBean(value = {ThrowableHandlerMethodInterceptor.class})
    public Advisor throwableHandlerAdvisor(ThrowableHandlerMethodInterceptor interceptor) {
        Pointcut pointcut = new AceAnnotationMatchingPointcut(ThrowableHandlerAspect.class, false);
        Advice advisor = interceptor;
        return new DefaultPointcutAdvisor(pointcut, advisor);
    }
}
