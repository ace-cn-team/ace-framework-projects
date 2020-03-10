package ace.fw.graphql.spring.webmvc;

import graphql.Assert;
import graphql.PublicApi;

import java.util.Collections;
import java.util.Map;
/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:02
 * @description
 */
public class GraphQLInvocationData {

    private final String query;
    private final String operationName;
    private final Map<String, Object> variables;

    public GraphQLInvocationData(String query, String operationName, Map<String, Object> variables) {
        this.query = Assert.assertNotNull(query, "query must be provided");
        this.operationName = operationName;
        this.variables = variables != null ? variables : Collections.emptyMap();
    }

    public String getQuery() {
        return query;
    }

    public String getOperationName() {
        return operationName;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
}
