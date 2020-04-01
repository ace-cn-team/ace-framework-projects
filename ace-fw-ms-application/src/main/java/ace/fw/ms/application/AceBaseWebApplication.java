package ace.fw.ms.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/10 17:59
 * @description
 */
@Slf4j
@SpringBootApplication
public class AceBaseWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(AceBaseWebApplication.class, args);
    }
}
