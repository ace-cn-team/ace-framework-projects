package ace.fw.graphql.gateway.federation.facade;

import ace.fw.graphql.gateway.federation.facade.model.request.GraphQLRequest;
import ace.fw.graphql.gateway.federation.facade.model.response.CommonResponse;
import ace.fw.graphql.gateway.federation.facade.model.response.ServiceResponse;
import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/2 16:27
 * @description
 */
public interface GraphQLService {
    @RequestLine("POST")
    @Headers("Content-Type: application/json")
    CommonResponse<ServiceResponse> getSchemaSDL(GraphQLRequest request);
}
