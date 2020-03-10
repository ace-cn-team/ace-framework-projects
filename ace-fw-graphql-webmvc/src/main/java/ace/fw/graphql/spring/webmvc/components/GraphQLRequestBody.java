package ace.fw.graphql.spring.webmvc.components;

import graphql.Internal;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:02
 * @description
 */
@Data
@Accessors(chain = true)
public class GraphQLRequestBody {
    private String query;
    private String operationName;
    private Map<String, Object> variables;
}
