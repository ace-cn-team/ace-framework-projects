package ace.fw.ms.application.support.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author caspar
 * @contract 279397942@qq.com
 * @create 2019/9/6 10:03
 * @description 监听服务是否启动成功, 并输出注册的bean
 */
@Slf4j
public class PrintBeansApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    WebApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        ConfigurableApplicationContext ctx = applicationReadyEvent.getApplicationContext();
        StringBuilder allBeanNameStr = new StringBuilder(500);
        allBeanNameStr.append("----------------- spring beans ------------------------");
        allBeanNameStr.append(System.lineSeparator());
        String[] beanNames = ctx.getBeanDefinitionNames();
        allBeanNameStr.append("beanNames 总数：" + beanNames.length);
        allBeanNameStr.append(System.lineSeparator());
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            String beanClassName = ctx.getBean(beanName).getClass().getName();
            allBeanNameStr.append("name:" + beanName + System.lineSeparator());
            allBeanNameStr.append("class:" + beanClassName + System.lineSeparator());
        }
        allBeanNameStr.append(System.lineSeparator());

        RequestMappingHandlerMapping mapping = applicationContext
                .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping
                .getHandlerMethods();
        for (RequestMappingInfo info : map.keySet()) {
            // 获取url的Set集合，一个方法可能对应多个url
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            for (String url : patterns) {
                //把结果存入静态变量中程序运行一次次方法之后就不用再次请求次方法
                allBeanNameStr.append(url);
                allBeanNameStr.append(System.lineSeparator());
            }
        }
        allBeanNameStr.append(System.lineSeparator());

        allBeanNameStr.append("------------------ end -----------------------");
        allBeanNameStr.append(System.lineSeparator());
        allBeanNameStr.append(String.format("########[微服务启动成功]########[%s]########",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        log.info(allBeanNameStr.toString());
    }
}
