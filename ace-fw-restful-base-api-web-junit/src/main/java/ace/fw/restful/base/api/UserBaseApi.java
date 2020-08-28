package ace.fw.restful.base.api;

import ace.fw.restful.base.api.web.junit.dal.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/28 17:10
 * @description
 */
@FeignClient(
        name = UserBaseApi.CONFIG_CLIENT_NAME,
        contextId = "UserBaseApi",
        path = "/" + UserBaseApi.MODULE_RESTFUL_NAME
)
@Validated
public interface UserBaseApi extends AbstractBaseApi<User, String> {
    String CONFIG_CLIENT_NAME = "${ace.ms.service.api.ace-fw-restful-base-api-web-junit.name:ace-fw-restful-base-api-web-junit}";
    String MODULE_RESTFUL_NAME = "user-base";
}
