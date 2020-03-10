package ace.fw.graphql.spring.webmvc.components;

import ace.fw.graphql.spring.webmvc.GraphQLInvocation;
import ace.fw.graphql.spring.webmvc.GraphQLInvocationData;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.WebRequest;

import java.util.concurrent.CompletableFuture;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:02
 * @description
 */
@Data
@Accessors(chain = true)
public class DefaultGraphQLInvocation implements GraphQLInvocation {

    private GraphQL graphQL;

    @Override
    public CompletableFuture<ExecutionResult> invoke(GraphQLInvocationData invocationData, WebRequest webRequest) {
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .query(invocationData.getQuery())
                .operationName(invocationData.getOperationName())
                .variables(invocationData.getVariables())
                .build();
        return graphQL.executeAsync(executionInput);
    }

}
