package ace.fw.json.jackson.autoconfigure;

import ace.fw.json.JsonPlugin;
import ace.fw.json.JsonUtils;
import ace.fw.json.jackson.JacksonPlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/6/28 16:11
 * @description 自动配置默认 {@link JsonPlugin} 使用jackson
 */
@ConditionalOnProperty(
        value = "ace.fw.json.jackson.enable",
        matchIfMissing = true
)
@Configuration
public class JacksonAutoConfigure {
    @Bean
    @ConditionalOnMissingBean
    public JsonPlugin jsonPlugin() {
        JacksonPlugin jacksonPlugin = new JacksonPlugin();
        JsonUtils.setJsonPlugin(jacksonPlugin);
        return jacksonPlugin;
    }
}
