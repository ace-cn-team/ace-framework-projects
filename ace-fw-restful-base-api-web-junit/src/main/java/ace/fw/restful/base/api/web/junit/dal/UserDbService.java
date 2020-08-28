package ace.fw.restful.base.api.web.junit.dal;

import ace.fw.restful.base.api.plugin.mybatisplus.impl.DbServiceImpl;
import ace.fw.restful.base.api.web.junit.dal.entity.User;
import ace.fw.restful.base.api.web.junit.dal.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/28 10:44
 * @description
 */
@Service
public class UserDbService extends DbServiceImpl<UserMapper, User> {
}
