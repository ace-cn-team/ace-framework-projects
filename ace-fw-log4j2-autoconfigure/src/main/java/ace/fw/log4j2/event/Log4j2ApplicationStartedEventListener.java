package ace.fw.log4j2.event;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/19 9:51
 * @description 重载spring boot logging配置的默认值,配置文件：  application-logging.properties
 */
public class Log4j2ApplicationStartedEventListener implements GenericApplicationListener {

    private final static int DEFAULT_ORDER = LoggingApplicationListener.DEFAULT_ORDER - 1;

    private final static Map<String, String> APPLICATION_PROPERTIES_MAP_TO_LOG4J2_CONFIG = new HashMap<>() {
        {
            put("ace.logging.console.level", "info");
        }
    };
    /**
     * 低于指定配置优先级的名称前缀
     */
    private final static String RELATIVE_PROPERTY_SOURCE_NAME_PREFIX = "applicationConfig";
    /**
     * 日志配置文件的PropertySource key
     */
    private final static String PROPERTY_SOURCE_NAME = "aceLoggingDefaultProperties";
    /**
     * 日志配置文件名称
     */
    private final static String PROPERTY_SOURCE_RESOURCE_PATH = "application-logging.properties";
    /**
     * spring 应用名称配置KEY
     */
    private final static String SPRING_APPLICATION_NAME = "spring.application.name";
    /**
     * 日志map配置优先级大于application-logging.properties配置文件
     */
    private final static String MAP_PROPERTY_SOURCE_NAME = "aceLoggingMapProperties";
    /**
     * 日志文件名称配置的KEY
     */
    private final static String PROPERTY_LOGGING_FILE_FILENAME = "logging.file.file-name";
    private static Class<?>[] EVENT_TYPES = {ApplicationStartingEvent.class, ApplicationEnvironmentPreparedEvent.class,
            ApplicationPreparedEvent.class, ContextClosedEvent.class, ApplicationFailedEvent.class};

    private static Class<?>[] SOURCE_TYPES = {SpringApplication.class, ApplicationContext.class};
    private static boolean isInit = false;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if ((event instanceof ApplicationEnvironmentPreparedEvent) == false) {
            return;
        }

        if (isInit) {
            return;
        }

        ConfigurableEnvironment environment = ((ApplicationEnvironmentPreparedEvent) event).getEnvironment();
        // 查找application.properties配置文件PropertySource
        Optional<PropertySource<?>> propertySourceOptional = this.findApplicationConfig(environment);

        if (propertySourceOptional.isEmpty()) {
            return;
        }

        PropertySource<?> applicationConfigPropertySource = propertySourceOptional.get();
        // 获取默认application-logging.properties配置文件PropertySource
        PropertySource<?> defaultLoggingPropertySource = this.getDefaultLoggingPropertySource();
        // 添加到配置环境中,优先级低于application.properties配置文件PropertySource
        environment.getPropertySources().addAfter(applicationConfigPropertySource.getName(), defaultLoggingPropertySource);
        // 处理日志名称，优先级：application-logging.properties默认值 < spring.application.name配置
        this.handleLoggingFileName(environment, applicationConfigPropertySource);

        isInit = true;
    }

    private void handleLoggingFileName(ConfigurableEnvironment environment, PropertySource<?> applicationConfigPropertySource) {
        Object applicationName = applicationConfigPropertySource.getProperty(SPRING_APPLICATION_NAME);
        if (applicationName == null) {
            return;
        }
        MapPropertySource mapPropertySource = new MapPropertySource(MAP_PROPERTY_SOURCE_NAME, new HashMap<String, Object>() {
            {
                put(PROPERTY_LOGGING_FILE_FILENAME, String.format("${%s}", SPRING_APPLICATION_NAME));
            }
        });
        environment.getPropertySources().addAfter(applicationConfigPropertySource.getName(), mapPropertySource);
    }

    private ResourcePropertySource getDefaultLoggingPropertySource() {
        EncodedResource EncodedResource = new EncodedResource(new ClassPathResource(PROPERTY_SOURCE_RESOURCE_PATH), "UTF-8");
        ResourcePropertySource resourcePropertySource = null;

        try {
            resourcePropertySource = new ResourcePropertySource(PROPERTY_SOURCE_NAME, EncodedResource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resourcePropertySource;
    }

    private Optional<PropertySource<?>> findApplicationConfig(ConfigurableEnvironment environment) {
        return environment.getPropertySources()
                .stream()
                .filter(p -> StringUtils.startsWith(p.getName(), RELATIVE_PROPERTY_SOURCE_NAME_PREFIX))
                .findFirst();
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        return isAssignableFrom(resolvableType.getRawClass(), EVENT_TYPES);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return isAssignableFrom(sourceType, SOURCE_TYPES);
    }

    private boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
        if (type != null) {
            for (Class<?> supportedType : supportedTypes) {
                if (supportedType.isAssignableFrom(type)) {
                    return true;
                }
            }
        }
        return false;
    }
}