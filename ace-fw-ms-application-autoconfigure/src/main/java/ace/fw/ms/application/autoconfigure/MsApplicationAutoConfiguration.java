package ace.fw.ms.application.autoconfigure;

import ace.fw.json.jackson.ObjectMapperFactory;
import ace.fw.ms.application.constant.AceWebApplicationBootstrapConstant;
import ace.fw.ms.application.controller.GlobalErrorRestControllerAdvice;
import ace.fw.ms.application.property.AceApplicationProperties;
import ace.fw.ms.application.support.handler.WebExceptionHandler;
import ace.fw.ms.application.support.listener.PrintBeansApplicationReadyEventListener;
import ace.fw.ms.application.support.resolver.WebExceptionResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/11 11:21
 * @description restful微服务架构，基础自动配置类
 */
@AutoConfigureBefore({ValidationAutoConfiguration.class})
@EnableConfigurationProperties(AceApplicationProperties.class)
@EnableWebMvc
@Configuration
public class MsApplicationAutoConfiguration implements WebMvcConfigurer, ErrorPageRegistrar, ApplicationContextAware {
    @Autowired
    private AceApplicationProperties aceApplicationProperties;
    private ApplicationContext applicationContext;
    private ResourceLoader resourceLoader;
    @Autowired(required = false)
    private MessageSource messageSource;


    //    @Bean
//    public MultipartResolver multipartResolver() {
//        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//        resolver.setMaxUploadSize(104857600);
//        resolver.setMaxInMemorySize(4096);
//        resolver.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
//        return resolver;
//    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        //resolvers.add(new CustomServletModelAttributeMethodProcessor());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//
//        MappedInterceptor errorHttpStatusCodeWrapMappedInterceptor = new MappedInterceptor(null, errorHttpStatusCodeWrapInterceptor);
//        registry.addInterceptor(errorHttpStatusCodeWrapMappedInterceptor);

    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        HandlerExceptionResolver exceptionResolver = this.aceWebExceptionResolver(this.webExceptionHandler());
        resolvers.add(exceptionResolver);
    }

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage errorPage = new ErrorPage(AceWebApplicationBootstrapConstant.MVC_DEFAULT_ERROR_PATH);
        registry.addErrorPages(errorPage);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.mediaType("json", MediaType.APPLICATION_JSON_UTF8);
        configurer.mediaType("html", MediaType.valueOf("text/html;charset=UTF-8"));
        configurer.mediaType("xml", MediaType.valueOf("application/xml;charset=UTF-8"));
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
        //扩展名至mimeType的映射,即 /user.json => application/json
        configurer.favorPathExtension(false);
        //用于开启 /userinfo/123?format=json 的支持
        configurer.favorParameter(true);
        configurer.parameterName("mediaType");
        configurer.ignoreAcceptHeader(true);
        configurer.useRegisteredExtensionsOnly(true);

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        //registry.addResourceHandler("/vendor/graphiql/**").addResourceLocations("classpath:/static/vendor/graphiql/");
        //        registry.addResourceHandler("/favicon.ico").addResourceLocations("/favicon.ico");
//        registry.addResourceHandler("/favicon.ico").addResourceLocations("/images/favicon.ico");
//        registry.addResourceHandler("/favicon.png").addResourceLocations("/images/favicon.png");
//        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926).resourceChain(true)
//                .addResolver(new GzipResourceResolver());
//        registry.addResourceHandler("/images/**").addResourceLocations("/images/").setCachePeriod(31556926);
//        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926).resourceChain(true).addResolver(new GzipResourceResolver());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        HttpMessageConverters httpMessageConverters = this.aceHttpMessageConverters();
        converters.addAll(httpMessageConverters.getConverters());
    }

    @Bean
    public HttpMessageConverters aceHttpMessageConverters() {
        HttpMessageConverters httpMessageConverters = new HttpMessageConverters(true, getConverters());
        return httpMessageConverters;
    }

    /**
     * enable default servlet
     *
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DefaultFormattingConversionService defaultFormattingConversionService = (DefaultFormattingConversionService) registry;
        defaultFormattingConversionService.setEmbeddedValueResolver(new EmbeddedValueResolver(((ConfigurableApplicationContext) this.applicationContext).getBeanFactory()));
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
//        registry.viewResolver(getJspViewResolver());
    }

    @Bean
    @ConditionalOnMissingBean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_JSON_UTF8,
                MediaType.valueOf("application/x-www-form-urlencoded; charset=UTF-8"),
                MediaType.valueOf("text/plain;charset=UTF-8"),
                MediaType.valueOf("text/html;charset=UTF-8")
        ));
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    @Bean
    @ConditionalOnMissingBean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    @Bean
    @ConditionalOnMissingBean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        return new ByteArrayHttpMessageConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return ObjectMapperFactory.getDefaultObjectMapper();
    }


    private List<HttpMessageConverter<?>> getConverters() {
        List<HttpMessageConverter<?>> myConverters = new ArrayList<>();
        myConverters.add(mappingJackson2HttpMessageConverter());
        myConverters.add(stringHttpMessageConverter());
        myConverters.add(byteArrayHttpMessageConverter());
        return myConverters;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /*private ViewResolver getJspViewResolver() {
        InternalResourceViewResolver jspViewResolver = new InternalResourceViewResolver();
        jspViewResolver.setPrefix("/WEB-INF/jsp/");
        jspViewResolver.setSuffix(".jsp");
        jspViewResolver.setOrder(1);
        jspViewResolver.setContentType("text/html;charset=UTF-8");
        return jspViewResolver;
    }*/
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(
            @Autowired Validator validator
    ) {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidator(validator);
        return methodValidationPostProcessor;
    }

    @Bean
    public Validator validator(@Autowired ValidatorFactory validatorFactory) {
        return validatorFactory.getValidator();
    }

    @Bean
    public ValidatorFactory validatorFactory() {

        HibernateValidatorConfiguration configuration = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true);

        if (messageSource != null) {
            configuration.messageInterpolator(new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource)));
        }

        return configuration.buildValidatorFactory();
    }

    @Bean
    public WebExceptionResolver aceWebExceptionResolver(
            @Autowired WebExceptionHandler webExceptionHandler) {
        return new WebExceptionResolver(mappingJackson2HttpMessageConverter(), webExceptionHandler);
    }

    @Bean
    public WebExceptionHandler webExceptionHandler() {
        return new WebExceptionHandler();
    }

    @Bean
    public PrintBeansApplicationReadyEventListener printBeansApplicationReadyEventListener() {
        PrintBeansApplicationReadyEventListener printBeansApplicationReadyEventListener = new PrintBeansApplicationReadyEventListener();
        printBeansApplicationReadyEventListener.setIsPrintBeanNames(aceApplicationProperties.getBeanNamePrinterEnable());
        printBeansApplicationReadyEventListener.setIsPrintMappingHandler(aceApplicationProperties.getMappingHandlerPrinterEnable());
        return printBeansApplicationReadyEventListener;
    }

    @Bean
    public GlobalErrorRestControllerAdvice globalErrorRestControllerAdvice(WebExceptionHandler webExceptionHandler) {
        return new GlobalErrorRestControllerAdvice(webExceptionHandler);
    }
}
