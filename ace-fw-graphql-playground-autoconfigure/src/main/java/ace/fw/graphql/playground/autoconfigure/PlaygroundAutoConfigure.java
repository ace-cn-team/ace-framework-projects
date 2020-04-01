package ace.fw.graphql.playground.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/1 16:33
 * @description
 */
@Configuration
public class PlaygroundAutoConfigure implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/vendor/playground/**").addResourceLocations("classpath:/static/vendor/playground/");
    }
}
