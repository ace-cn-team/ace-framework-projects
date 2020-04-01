package ace.fw.graphql.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/1 14:36
 * @description
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(value = "ace.graphql.sdl")
public class GraphqlSDLProperty {
    /**
     * graphql schema文件位置，默认根位置：classpath
     */
    private List<String> schemaFiles = Arrays.asList("graphqls/schema.graphqls");
}
