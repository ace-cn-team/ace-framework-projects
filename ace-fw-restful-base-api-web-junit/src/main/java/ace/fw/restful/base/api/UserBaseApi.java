package ace.fw.restful.base.api;

import ace.fw.model.response.GenericResponseExt;
import ace.fw.restful.base.api.web.junit.dal.entity.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/28 17:10
 * @description
 */
@FeignClient(
        name = UserBaseApi.MODULE_RESTFUL_NAME,
        contextId = "UserBaseApi",
        path = "/" + UserBaseApi.MODULE_RESTFUL_NAME,
        url = "http://localhost:8080"
)
@Validated
public interface UserBaseApi extends AbstractBaseApi<User, String> {
    String CONFIG_CLIENT_NAME = "${ace.ms.service.api.ace-fw-restful-base-api-web-junit.name:ace-fw-restful-base-api-web-junit}";
    String MODULE_RESTFUL_NAME = "user-base";

    @RequestMapping(path = "/find-one-by-level", method = RequestMethod.POST)
    GenericResponseExt<List<User>> findOneByLevel(@Valid @RequestBody Long level);

}
