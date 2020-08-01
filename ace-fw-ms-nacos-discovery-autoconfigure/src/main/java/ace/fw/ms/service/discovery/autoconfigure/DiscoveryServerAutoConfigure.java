package ace.fw.ms.service.discovery.autoconfigure;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/3/11 12:32
 * @description
 */
@PropertySource("classpath:application-nacos-discovery.properties")
@EnableDiscoveryClient
@Configuration
@Slf4j
public class DiscoveryServerAutoConfigure {

}
