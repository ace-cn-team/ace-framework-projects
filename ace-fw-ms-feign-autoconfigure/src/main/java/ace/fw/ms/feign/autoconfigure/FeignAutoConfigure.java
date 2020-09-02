package ace.fw.ms.feign.autoconfigure;

import ace.fw.json.jackson.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/1 16:05
 * @description
 */
@PropertySource("classpath:application-feign.properties")
@Configuration
public class FeignAutoConfigure {
    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return ObjectMapperFactory.getDefaultObjectMapper();
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
}
