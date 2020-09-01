package ace.fw.restful.base.api.web.junit.controller;

import ace.fw.model.response.GenericResponseExt;
import ace.fw.restful.base.api.web.AbstractMybatisController;
import ace.fw.restful.base.api.web.junit.dal.entity.User;
import ace.fw.util.GenericResponseExtUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/28 17:16
 * @description
 */
@RestController
public class UserControllerImpl extends AbstractMybatisController<User, String> implements UserController {


    @Override
    public GenericResponseExt<List<User>> findOneByLevel(@Valid Long level) {
        List<User> user = this.getDbService().lambdaQuery()
                .le(User::getLevel, level)
                .list();
        return GenericResponseExtUtils.buildSuccessWithData(user);
    }
}
