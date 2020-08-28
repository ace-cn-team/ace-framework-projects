package ace.fw.restful.base.api.web.junit.controller;

import ace.fw.restful.base.api.AbstractBaseApi;
import ace.fw.restful.base.api.UserBaseApi;
import ace.fw.restful.base.api.web.AbstractController;
import ace.fw.restful.base.api.web.junit.dal.UserDbService;
import ace.fw.restful.base.api.web.junit.dal.entity.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/28 17:16
 * @description
 */
@RestController
public class UserControllerImpl extends AbstractController<User, UserDbService, String> implements UserController {
}
