package ace.fw.graphql.schema.sdl.autoconfigure;
import com.apollographql.federation.graphqljava.Federation;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
