package ace.fw.logic.common.aop.Interceptor.handler;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/4 10:52
 * @description 异常处理接口
 */
public interface ThrowableHandler<Result> {
    Result handle(MethodInvocation methodInvocation, Throwable throwable);
}
