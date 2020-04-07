package ace.fw.graphql.gateway.federation.impl;

import ace.fw.graphql.gateway.federation.SchemaLoader;
import ace.fw.graphql.gateway.federation.facade.GraphQLService;
import ace.fw.graphql.gateway.federation.facade.model.request.GraphQLRequest;
import ace.fw.graphql.gateway.federation.property.GraphQLServerProperty;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/2 14:29
 * @description
 */
@Slf4j
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FederationUrlSchemaLoaderImpl implements SchemaLoader {

    private GraphQLServerProperty graphQLServerProperty;
    private GraphQLService graphQLService;

    @Override
    public TypeDefinitionRegistry load() {
        SchemaParser schemaParser = new SchemaParser();
        GraphQLRequest request = GraphQLRequest.builder()
                .operationName(null)
                .query("{service:_service{sdl}}")
                .variables(new HashMap<>())
                .build();
        try {
            String sdl = graphQLService.getSchemaSDL(request).getData().getService().getSdl();
            return schemaParser.parse(sdl);
        } catch (Exception e) {
            String msg = String.format("获取%s-%s的SDL失败", graphQLServerProperty.getName(), graphQLServerProperty.getUrl());
            throw new RuntimeException(msg, e);
        }
    }
}
