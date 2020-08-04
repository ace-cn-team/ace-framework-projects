package ace.fw.logic.common.aop.extension;

import ace.fw.logic.common.aop.Interceptor.log.annotations.LogAspect;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/3 18:44
 * @description 类似 {@link AnnotationMatchingPointcut}
 * 区别：
 * 1.不拦截默认方法:
 * toString
 * hashCode
 * <p>
 * 2.会匹配方法与类上面的注解
 */

public class AceAnnotationMatchingPointcut implements Pointcut {
    private final Class annotationType;
    private final AnnotationMatchingPointcut methodPointcut;
    private final ClassFilter classFilter;
    private final MethodMatcher methodMatcher;
    private final static List<String> excludeMethodList = Arrays.asList(
            "equals",
            "hashCode",
            "toString",
            "getClass",
            "wait",
            "notifyAll",
            "notify"
    );

    public AceAnnotationMatchingPointcut(Class annotationType) {
        this(annotationType, false);
    }

    public AceAnnotationMatchingPointcut(Class annotationType, boolean checkInherited) {
        this.annotationType = annotationType;
        this.methodPointcut = new AnnotationMatchingPointcut(null, this.annotationType, checkInherited);
        this.classFilter = aClass -> methodPointcut.getClassFilter().matches(aClass);
        this.methodMatcher = new MethodMatcher() {
            @Override
            public boolean matches(Method method, Class<?> aClass) {
                if (isExcludeMethod(method)) {
                    return false;
                }
                return methodPointcut.getMethodMatcher().matches(method, aClass) ||
                        AnnotationUtils.findAnnotation(aClass, LogAspect.class) != null;
            }

            @Override
            public boolean isRuntime() {
                return methodPointcut.getMethodMatcher().isRuntime();
            }

            @Override
            public boolean matches(Method method, Class<?> aClass, Object... objects) {
                if (isExcludeMethod(method)) {
                    return false;
                }
                return methodPointcut.getMethodMatcher().matches(method, aClass, objects) ||
                        AnnotationUtils.findAnnotation(aClass, LogAspect.class) != null;
            }
        };
    }

    @Override
    public ClassFilter getClassFilter() {
        return this.classFilter;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this.methodMatcher;
    }

    private boolean isExcludeMethod(Method method) {
        String methodName = method.getName();
        return excludeMethodList.stream().anyMatch(p -> StringUtils.equals(p, methodName));
    }
}
