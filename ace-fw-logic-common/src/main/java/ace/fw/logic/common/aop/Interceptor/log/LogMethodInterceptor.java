package ace.fw.logic.common.aop.Interceptor.log;

import ace.fw.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.Order;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/3 10:27
 * @description 逻辑层日志切面
 */
@Order(2)
@Slf4j
public class LogMethodInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        Object result = null;
        Throwable throwable = null;
        try {
            result = methodInvocation.proceed();
        } catch (Throwable ex) {
            throwable = ex;
        }

        try {
            this.logInfo(methodInvocation.getMethod().getName(), methodInvocation.getThis().getClass().getName(), methodInvocation.getArguments(), result, throwable);
        } catch (Throwable ex) {
            log.error("日志[LogAspect]异常", ex);
        }

        if (throwable != null) {
            throw throwable;
        }

        return result;
    }

    private void logInfo(String methodName, String targetClassName, Object[] params, Object result, Throwable throwable) throws NoSuchMethodException {
        StringBuilder sb = new StringBuilder();
        // 获取目标方法的名称
        String inputParams = JsonUtils.toJson(params);
        String outParams = JsonUtils.toJson(result);
        String exceptionParams = throwable == null ? "无" : "有";
        sb.append("\r\n-------[${targetClassName}][${methodName}]方法开始------\r\n");
        sb.append("类名:${targetClassName}\r\n");
        sb.append("方法:${methodName}\r\n");
        sb.append("入参:${inputParams}\r\n");
        sb.append("出参:${outParams}\r\n");
        sb.append("异常:${exceptionParams}\r\n");
        sb.append("-------[${targetClassName}][${methodName}]方法结束------\r\n");
        String logString = sb.toString()
                .replaceAll("\\$\\{targetClassName}", targetClassName)
                .replaceAll("\\$\\{methodName}", methodName)
                .replaceAll("\\$\\{inputParams}", inputParams)
                .replaceAll("\\$\\{outParams}", outParams)
                .replaceAll("\\$\\{exceptionParams}", exceptionParams);

        if (throwable == null) {
            log.info(logString);
        } else {
            log.error(logString, throwable);
        }
    }


    public static void main(String[] args) {
        String tpl = "[${targetClassName}][${methodName}]${targetClassName}"
                .replaceAll("\\$\\{targetClassName}", "111");
        System.out.println(tpl);
    }

}
