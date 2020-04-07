import ace.fw.graphql.gateway.federation.GraphQLSchemaBuilder;
import ace.fw.graphql.gateway.federation.SchemaLoader;
import ace.fw.graphql.gateway.federation.facade.DefaultServiceFactory;
import ace.fw.graphql.gateway.federation.facade.GraphQLService;
import ace.fw.graphql.gateway.federation.impl.FederationUrlSchemaLoaderImpl;
import ace.fw.graphql.gateway.federation.property.GraphQLServerProperty;
import graphql.schema.GraphQLSchema;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/2 16:48
 * @description
 */
public class Test {
    public static void main(String[] args) {
        List<SchemaLoader> schemaFetchers = new ArrayList<>();
        SchemaLoader schemaLoader2 = FederationUrlSchemaLoaderImpl
                .builder()
                .graphQLServerProperty(GraphQLServerProperty.builder()
                        .name("8082")
                        .url("http://localhost:8082/graphql")
                        .build())
                .graphQLService(DefaultServiceFactory.get("http://localhost:8082/graphql", GraphQLService.class))
                .build();
        SchemaLoader schemaLoader1 = FederationUrlSchemaLoaderImpl
                .builder()
                .graphQLServerProperty(GraphQLServerProperty.builder()
                        .name("8081")
                        .url("http://localhost:8081/graphql")
                        .build())
                .graphQLService(DefaultServiceFactory.get("http://localhost:8081/graphql", GraphQLService.class))
                .build();
        schemaFetchers.add(schemaLoader1);
        schemaFetchers.add(schemaLoader2);
        GraphQLSchemaBuilder graphQLSchemaBuilder = GraphQLSchemaBuilder
                .builder()
                .schemaLoaders(schemaFetchers)
                .build();

        GraphQLSchema graphQLSchema = graphQLSchemaBuilder.buildGraphQLSchema();
    }
}
