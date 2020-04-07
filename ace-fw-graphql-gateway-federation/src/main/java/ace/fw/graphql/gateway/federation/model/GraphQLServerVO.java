package ace.fw.graphql.gateway.federation.model;

import ace.fw.graphql.gateway.federation.property.GraphQLServerProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/2 11:47
 * @description
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GraphQLServerVO {
    private GraphQLServerProperty graphQLServerProperty;
    private String sdl;
}
