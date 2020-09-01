package ace.fw.restful.base.api.plugin.mybatisplus;

import ace.fw.restful.base.api.plugin.DbService;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/31 16:53
 * @description
 */
public interface MybatisPlusDbService<T, IdType> extends DbService<T, IdType>, IService<T> {
}
