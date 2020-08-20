package ace.fw.log4j2.event;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
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
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/19 9:51
 * @description 重载spring boot logging配置的默认值,配置文件：  application-logging.properties
 */
public class Log4j2ConfigApplicationStartedEventListener implements GenericApplicationListener {

    private final static int DEFAULT_ORDER = LoggingApplicationListener.DEFAULT_ORDER - 1;

    private final static Map<String, String> APPLICATION_PROPERTIES_MAP_TO_LOG4J2_CONFIG = new HashMap<>() {
        {
            put("ace.logging.console.level", "info");
        }
    };
    /**
     * applicationConfig配置的名称前缀
     */
    private final static String APPLICATION_CONFIG_PROPERTY_SOURCE_NAME_PREFIX = "applicationConfig";
    /**
     * 默认配置名称
     */
    private final static String DEFAULT_PROPERTY_SOURCE_NAME = "defaultProperties";
    /**
     * 日志配置文件的PropertySource key
     */
    private final static String APPLICATION_PROPERTY_SOURCE_NAME = "aceLoggingDefaultProperties";
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

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if ((event instanceof ApplicationEnvironmentPreparedEvent) == false) {
            return;
        }

        ConfigurableEnvironment environment = ((ApplicationEnvironmentPreparedEvent) event).getEnvironment();
        // 初始化默认配置
        this.initDefaultPropertySource(environment);
        // 处理日志名称配置
        this.handleLoggingFileName(environment);

    }

    /**
     * 处理日志文件名称配置
     *
     * @param environment
     */
    private void handleLoggingFileName(ConfigurableEnvironment environment) {
        // 查找application.properties配置文件PropertySource
        Optional<MapPropertySource> propertySourceOptional = this.findApplicationConfig(environment);
        if (propertySourceOptional.isEmpty()) {
            return;
        }
        MapPropertySource applicationConfigPropertySource = propertySourceOptional.get();
        // 处理日志名称，优先级：application-logging.properties默认值 < spring.application.name配置
        this.addLoggingFileNamePropertySourceToEnvironment(environment, applicationConfigPropertySource);
    }

    /**
     * 添加配置到环境中，配置优先级高于相对配置
     *
     * @param environment
     * @param relativePropertyName
     * @param propertySource
     */
    private void addPropertySourceToEnvironmentBefore(ConfigurableEnvironment environment, PropertySource<?> relativePropertyName, PropertySource<?> propertySource) {
        environment.getPropertySources().addBefore(relativePropertyName.getName(), propertySource);
    }

    /**
     * 添加配置到环境中，配置优先级低于相对配置
     *
     * @param environment
     * @param relativePropertyName
     * @param propertySource
     */
    private void addPropertySourceToEnvironmentAfter(ConfigurableEnvironment environment, PropertySource<?> relativePropertyName, PropertySource<?> propertySource) {
        environment.getPropertySources().addAfter(relativePropertyName.getName(), propertySource);
    }

    /**
     * 初始化默认配置
     *
     * @param environment
     */
    private void initDefaultPropertySource(ConfigurableEnvironment environment) {
        // 获取默认application-logging.properties配置文件PropertySource
        ResourcePropertySource defaultLoggingPropertySource = this.getDefaultLoggingPropertySource();
        // 获取默认配置
        PropertySource<?> defaultPropertySource = this.findDefaultPropertySource(environment);
        // 添加到配置环境中,优先级高于指定配置
        this.addPropertySourceToEnvironmentBefore(environment, defaultPropertySource, defaultLoggingPropertySource);
        // 添加配置到log4j2 mdc
        this.addConfigToLog4j2MDC(environment, Arrays.asList(defaultLoggingPropertySource.getPropertyNames()));

    }

    /**
     * 获取默认配置
     *
     * @param environment
     * @return
     */
    private PropertySource<?> findDefaultPropertySource(ConfigurableEnvironment environment) {
        PropertySource<?> propertySource = environment.getPropertySources().get(DEFAULT_PROPERTY_SOURCE_NAME);
        Assert.notNull(propertySource, "default propertySource must not be null");
        return propertySource;
    }

    private void addConfigToLog4j2MDC(ConfigurableEnvironment environment, List<String> propertyNames) {
        for (String propertyName : propertyNames) {
            MDC.put(propertyName, environment.getProperty(propertyName));
        }
        refreshConfigLog4j2MDC(environment);
    }

    private void refreshConfigLog4j2MDC(ConfigurableEnvironment environment) {
        Map<String, String> map = MDC.getCopyOfContextMap();
        map.entrySet().stream()
                .forEach(entry -> {
                    MDC.put(entry.getKey(), environment.getProperty(entry.getKey()));
                });
    }

    private void addLoggingFileNamePropertySourceToEnvironment(ConfigurableEnvironment environment, PropertySource<?> applicationConfigPropertySource) {
        Object applicationName = applicationConfigPropertySource.getProperty(SPRING_APPLICATION_NAME);
        if (applicationName == null) {
            return;
        }
        MapPropertySource mapPropertySource = new MapPropertySource(MAP_PROPERTY_SOURCE_NAME, new HashMap<String, Object>() {
            {
                put(PROPERTY_LOGGING_FILE_FILENAME, String.format("${%s}.log", SPRING_APPLICATION_NAME));
            }
        });
        this.addPropertySourceToEnvironmentAfter(environment, applicationConfigPropertySource, mapPropertySource);
        // 添加配置映射到log4j2的MDC
        this.addConfigToLog4j2MDC(environment, Arrays.asList(mapPropertySource.getPropertyNames()));
    }

    /**
     * 获取默认application-logging.properties配置文件PropertySource
     *
     * @return
     */
    private ResourcePropertySource getDefaultLoggingPropertySource() {
        EncodedResource EncodedResource = new EncodedResource(new ClassPathResource(PROPERTY_SOURCE_RESOURCE_PATH), "UTF-8");
        ResourcePropertySource resourcePropertySource = null;

        try {
            resourcePropertySource = new ResourcePropertySource(APPLICATION_PROPERTY_SOURCE_NAME, EncodedResource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resourcePropertySource;
    }

    private Optional<MapPropertySource> findApplicationConfig(ConfigurableEnvironment environment) {
        return environment.getPropertySources()
                .stream()
                .filter(p -> StringUtils.startsWith(p.getName(), APPLICATION_CONFIG_PROPERTY_SOURCE_NAME_PREFIX))
                .map(p -> (MapPropertySource) p)
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