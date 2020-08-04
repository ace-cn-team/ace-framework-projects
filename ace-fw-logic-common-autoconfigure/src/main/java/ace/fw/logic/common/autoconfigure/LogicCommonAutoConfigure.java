package ace.fw.logic.common.autoconfigure;

import ace.fw.logic.common.aop.Interceptor.log.LogMethodInterceptor;
import ace.fw.logic.common.aop.Interceptor.log.annotations.LogAspect;
import ace.fw.logic.common.aop.extension.AceAnnotationMatchingPointcut;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/2/5 14:38
 * @description
 */
@Configuration
public class LogicCommonAutoConfigure {
    public final static String CONFIG_LOGIC_COMMON_LOG_ASPECT_ENABLE = "ace.fw.logic.common.log-aspect.enable";

    @Bean
    @ConditionalOnProperty(value = CONFIG_LOGIC_COMMON_LOG_ASPECT_ENABLE, matchIfMissing = true, havingValue = "true")
    @ConditionalOnMissingBean
    public LogMethodInterceptor logMethodInterceptor() {
        LogMethodInterceptor logMethodInterceptor = new LogMethodInterceptor();
        return logMethodInterceptor;
    }

    @Bean
    @ConditionalOnBean(value = {LogMethodInterceptor.class})
    public Advisor logAdvisor(LogMethodInterceptor logMethodInterceptor) {
        Pointcut pointcut = new AceAnnotationMatchingPointcut(LogAspect.class, false);
        Advice advisor = logMethodInterceptor;
        return new DefaultPointcutAdvisor(pointcut, advisor);
    }
}
