package ace.fw.restful.base.api.web.junit;

import ace.fw.restful.base.api.UserBaseApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/27 17:04
 * @description
 */
@ConditionalOnProperty(
        name = UserBaseApi.CONFIG_CLIENT_NAME,
        havingValue = "true",
        matchIfMissing = true
)
@EnableFeignClients(basePackages = {"ace.fw.restful.base.api"})
@Configuration
@SpringBootApplication
public class JUnitApplication {
    public static void main(String[] args) {
        SpringApplication.run(JUnitApplication.class, args);
    }
}
