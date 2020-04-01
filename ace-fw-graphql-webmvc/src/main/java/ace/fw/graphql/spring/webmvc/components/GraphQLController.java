package ace.fw.graphql.spring.webmvc.components;


import ace.fw.graphql.spring.webmvc.ExecutionResultHandler;
import ace.fw.graphql.spring.webmvc.GraphQLInvocation;
import ace.fw.graphql.spring.webmvc.GraphQLInvocationData;
import ace.fw.json.JsonPlugin;
import graphql.ExecutionResult;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:02
 * @description
 */
@Data
@Accessors(chain = true)
@RestController
public class GraphQLController {

    GraphQLInvocation graphQLInvocation;

    ExecutionResultHandler executionResultHandler;

    JsonPlugin jsonPlugin;

    @RequestMapping(value = "${graphql.url:graphql}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object graphqlPOST(@RequestBody GraphQLRequestBody body,
                              WebRequest webRequest) {
        String query = body.getQuery();
        if (query == null) {
            query = "";
        }
        CompletableFuture<ExecutionResult> executionResult = graphQLInvocation.invoke(new GraphQLInvocationData(query, body.getOperationName(), body.getVariables()), webRequest);
        return executionResultHandler.handleExecutionResult(executionResult);
    }

    @RequestMapping(value = "${graphql.url:graphql}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object graphqlGET(
            @RequestParam("query") String query,
            @RequestParam(value = "operationName", required = false) String operationName,
            @RequestParam(value = "variables", required = false) String variablesJson,
            WebRequest webRequest) {
        CompletableFuture<ExecutionResult> executionResult = graphQLInvocation.invoke(new GraphQLInvocationData(query, operationName, convertVariablesJson(variablesJson)), webRequest);
        return executionResultHandler.handleExecutionResult(executionResult);
    }

    private Map<String, Object> convertVariablesJson(String jsonMap) {
        if (jsonMap == null) return Collections.emptyMap();

        return jsonPlugin.toMap(jsonMap, String.class, Object.class);

    }


}
