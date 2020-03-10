package ace.fw.restful.base.api.web.mybatis.plus.autoconfigure.resolver;

import ace.fw.data.model.entity.Entity;
import ace.fw.mybatis.plus.extension.model.EntityConfigInfo;
import ace.fw.mybatis.plus.extension.model.EntityServiceInfo;

import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/3 14:30
 * @description {@link ace.fw.data.service.DbService} 解析系统业务接口，匹配到通用接口,默认解析实现{@link }
 */
public interface EntityConfigResolver {
    Map<String, EntityConfigInfo<Entity>> resolve();
}
