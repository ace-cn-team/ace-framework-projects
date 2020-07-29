package ace.fw.jetcache.redisson.junit;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/3 15:21
 * @description
 */
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = {"ace.fw.jetcache.redisson.junit.service"})
@SpringBootApplication
public class JUnitApplication {
    public static void main(String[] args) {
        SpringApplication.run(JUnitApplication.class, args);
    }
}
