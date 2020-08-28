package ace.fw.restful.base.api.web.junit.dal.mapper;

import ace.fw.mybatis.plus.extension.mapper.AceBaseMapper;
import ace.fw.restful.base.api.web.junit.dal.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/28 10:46
 * @description
 */
@Mapper
public interface UserMapper extends AceBaseMapper<User> {
}
