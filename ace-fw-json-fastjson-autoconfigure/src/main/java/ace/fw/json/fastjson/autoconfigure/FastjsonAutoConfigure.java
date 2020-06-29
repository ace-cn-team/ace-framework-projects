package ace.fw.json.fastjson.autoconfigure;

import ace.fw.json.JsonPlugin;
import ace.fw.json.JsonUtils;
import ace.fw.json.fastjson.FastJsonPlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/6/28 16:11
 * @description 自动配置默认 {@link JsonPlugin} 使用fastjson
 */
@ConditionalOnProperty(
        value = "ace.fw.json.fastjson.enable",
        matchIfMissing = true
)
@Configuration
public class FastjsonAutoConfigure {
    @ConditionalOnMissingBean
    @Bean
    public JsonPlugin jsonPlugin() {
        FastJsonPlugin fastJsonPlugin = new FastJsonPlugin();
        JsonUtils.setJsonPlugin(fastJsonPlugin);
        return fastJsonPlugin;
    }
}
