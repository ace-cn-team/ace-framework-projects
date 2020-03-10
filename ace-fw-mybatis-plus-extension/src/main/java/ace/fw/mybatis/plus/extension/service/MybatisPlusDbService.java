package ace.fw.mybatis.plus.extension.service;

import ace.fw.data.model.entity.Entity;
import ace.fw.data.service.DbService;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/8 10:38
 * @description IService封装
 */
public interface MybatisPlusDbService<T extends Entity> extends IService<T>, DbService<T> {

}
