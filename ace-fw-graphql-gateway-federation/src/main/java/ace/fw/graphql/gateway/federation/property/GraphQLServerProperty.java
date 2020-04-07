package ace.fw.graphql.gateway.federation.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/2 10:55
 * @description
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GraphQLServerProperty {
    /**
     * graphql server 访问接口url
     */
    private String url;
    /**
     * graphql server 名称
     */
    private String name;
}
