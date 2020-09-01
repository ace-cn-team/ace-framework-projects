package ace.fw.restful.base.api;

import ace.fw.model.response.GenericResponseExt;
import ace.fw.restful.base.api.web.junit.dal.entity.Profile;
import ace.fw.restful.base.api.web.junit.dal.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/28 17:10
 * @description
 */
@FeignClient(
        name = ProfileBaseApi.MODULE_RESTFUL_NAME,
        contextId = "ProfileBaseApi",
        path = "/" + ProfileBaseApi.MODULE_RESTFUL_NAME,
        url = "http://localhost:8080"
)
@Validated
public interface ProfileBaseApi extends AbstractBaseApi<Profile, String> {
    String MODULE_RESTFUL_NAME = "profile-base";

    @RequestMapping(path = "/find-one-by-level", method = RequestMethod.POST)
    GenericResponseExt<Profile> findOneByLevel(@Valid @RequestBody Long level);

}
