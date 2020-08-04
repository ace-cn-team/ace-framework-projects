package ace.fw.logic.common.aop.Interceptor.log.annotations;

import java.lang.annotation.*;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/3 10:28
 * @description 自动为方法添加日志切面
 * 功能：
 * 打印入参、出参、调用结果、异常日志
 */
@Documented
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LogAspect {

}