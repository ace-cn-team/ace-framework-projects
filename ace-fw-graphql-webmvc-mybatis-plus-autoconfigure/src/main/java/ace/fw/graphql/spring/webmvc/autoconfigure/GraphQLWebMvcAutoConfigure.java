package ace.fw.graphql.spring.webmvc.autoconfigure;

import ace.fw.graphql.spring.Mutation;
import ace.fw.graphql.spring.Query;
import ace.fw.graphql.spring.Subscription;
import ace.fw.graphql.spring.webmvc.ExecutionResultHandler;
import ace.fw.graphql.spring.webmvc.GraphQLInvocation;
import ace.fw.graphql.spring.webmvc.components.DefaultExecutionResultHandler;
import ace.fw.graphql.spring.webmvc.components.DefaultGraphQLInvocation;
import ace.fw.graphql.spring.webmvc.components.GraphQLController;
import ace.fw.json.JsonPlugin;
import graphql.GraphQL;
import graphql.annotations.AnnotationsSchemaCreator;
import graphql.schema.GraphQLSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/9 17:49
 * @description
 */
@Configuration
public class GraphQLWebMvcAutoConfigure {

    @Bean
    public GraphQLSchema graphQLSchema(
            @Autowired(required = true) Query query,
            @Autowired(required = false) Mutation mutation,
            @Autowired(required = false) Subscription subscription) {

        AnnotationsSchemaCreator.Builder builder = AnnotationsSchemaCreator.newAnnotationsSchema();

        builder.query(query.getClass());

        if (mutation != null) {
            builder.mutation(mutation.getClass());
        }

        if (subscription != null) {
            builder.subscription(subscription.getClass());
        }

        builder.setAlwaysPrettify(true);

        return builder.build();
    }

    @Bean
    public GraphQL graphQL(GraphQLSchema graphQLSchema) {
        GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema)
                .build();
        return graphQL;
    }

    @Bean
    public GraphQLInvocation graphQLInvocation(GraphQL graphQL) {
        GraphQLInvocation graphQLInvocation = new DefaultGraphQLInvocation()
                .setGraphQL(graphQL);
        return graphQLInvocation;
    }

    @Bean
    public ExecutionResultHandler executionResultHandler(JsonPlugin jsonPlugin) {
        ExecutionResultHandler executionResultHandler = new DefaultExecutionResultHandler()
                .setJsonPlugin(jsonPlugin);

        return executionResultHandler;
    }

    @Bean
    public GraphQLController graphQLController(JsonPlugin jsonPlugin,
                                               GraphQLInvocation graphQLInvocation,
                                               ExecutionResultHandler executionResultHandler) {
        GraphQLController graphQLController = new GraphQLController()
                .setJsonPlugin(jsonPlugin)
                .setGraphQLInvocation(graphQLInvocation)
                .setExecutionResultHandler(executionResultHandler);
        return graphQLController;
    }
}
