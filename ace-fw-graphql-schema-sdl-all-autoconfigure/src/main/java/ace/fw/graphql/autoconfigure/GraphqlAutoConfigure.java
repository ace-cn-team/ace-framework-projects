package ace.fw.graphql.autoconfigure;

import ace.fw.graphql.properties.GraphqlSDLProperty;
import com.apollographql.federation.graphqljava.Federation;
import com.apollographql.federation.graphqljava._Entity;
import graphql.kickstart.execution.config.DefaultGraphQLSchemaProvider;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import graphql.servlet.osgi.GraphQLMutationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/3/30 15:59
 * @description
 */
@EnableConfigurationProperties(GraphqlSDLProperty.class)
@Configuration
public class GraphqlAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    public GraphQLSchema graphQLSchema(List<GraphQLResolver<?>> resolvers, GraphqlSDLProperty graphqlSDLProperty) {
        String[] schemaFile = graphqlSDLProperty.getSchemaFiles().toArray(new String[graphqlSDLProperty.getSchemaFiles().size()]);

        SchemaParser schemaParser = SchemaParser.newParser()
                .resolvers(resolvers)
                .files(schemaFile)
                .build();

        GraphQLSchema graphQLSchema = schemaParser.makeExecutableSchema();

        graphQLSchema = Federation.transform(graphQLSchema).build();

        return graphQLSchema;
    }
}
