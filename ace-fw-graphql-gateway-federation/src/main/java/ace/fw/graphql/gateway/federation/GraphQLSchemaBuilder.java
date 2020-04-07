package ace.fw.graphql.gateway.federation;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/2 10:53
 * @description
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GraphQLSchemaBuilder {

    private List<SchemaLoader> schemaLoaders;

    public GraphQLSchema buildGraphQLSchema() {

        TypeDefinitionRegistry typeDefinitionRegistry = this.mergeTypeDefinitionRegistry();

        RuntimeWiring runtimeWiring = this.buildRuntimeWiring(typeDefinitionRegistry);

        SchemaGenerator schemaGenerator = new SchemaGenerator();

        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        return graphQLSchema;
    }

    private RuntimeWiring buildRuntimeWiring(TypeDefinitionRegistry typeDefinitionRegistry) {

        return RuntimeWiring.newRuntimeWiring()
                .type("", typeWiring -> {
                    typeWiring.defaultDataFetcher(new DataFetcher() {
                        @Override
                        public Object get(DataFetchingEnvironment environment) throws Exception {
                            //  environment.get
                            return null;
                        }
                    });
                    return typeWiring;
                })
                .build();
    }

    private TypeDefinitionRegistry mergeTypeDefinitionRegistry() {
        List<TypeDefinitionRegistry> typeDefinitionRegistryList = new ArrayList<>();

        for (int i = 0; i < schemaLoaders.size(); i++) {
            SchemaLoader schemaLoader = schemaLoaders.get(i);
            TypeDefinitionRegistry newTypeDefinitionRegistry = schemaLoader.load();
            typeDefinitionRegistryList.add(newTypeDefinitionRegistry);
        }

        TypeDefinitionRegistryBuilder typeDefinitionRegistryBuilder = TypeDefinitionRegistryBuilder
                .builder()
                .typeDefinitionRegistries(typeDefinitionRegistryList)
                .build();

        TypeDefinitionRegistry mergeTypeDefinitionRegistry = typeDefinitionRegistryBuilder.merge();

        return mergeTypeDefinitionRegistry;
    }
}
