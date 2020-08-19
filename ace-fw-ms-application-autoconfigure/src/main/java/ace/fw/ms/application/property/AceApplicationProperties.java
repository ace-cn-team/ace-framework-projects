package ace.fw.ms.application.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/17 17:16
 * @description
 */
@ConfigurationProperties(AceApplicationProperties.PREFIX)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AceApplicationProperties {
    public final static String PREFIX = "ace.application";
    /**
     * 是否打印初始化的bean信息
     */
    private Boolean beanNamePrinterEnable = false;
    /**
     * 是否打印 http mapping 与handler的信息
     */
    private Boolean mappingHandlerPrinterEnable = false;
}
