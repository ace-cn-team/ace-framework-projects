package ace.fw.restful.base.api.web.junit.controller;

import ace.fw.model.response.GenericResponseExt;
import ace.fw.restful.base.api.web.AbstractMybatisController;
import ace.fw.restful.base.api.web.junit.dal.entity.Profile;
import ace.fw.util.GenericResponseExtUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/28 17:16
 * @description
 */
@RestController
public class ProfileControllerImpl extends AbstractMybatisController<Profile, String> implements ProfileController {


    @Override
    public GenericResponseExt<Profile> findOneByLevel(@Valid Long level) {
        Profile profile = this.getDbService().lambdaQuery()
                .le(Profile::getLevel, Long.MAX_VALUE)
                .one();
        return GenericResponseExtUtils.buildSuccessWithData(profile);
    }
}
