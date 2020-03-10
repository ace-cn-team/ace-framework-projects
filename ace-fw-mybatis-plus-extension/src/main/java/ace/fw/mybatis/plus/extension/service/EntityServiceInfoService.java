package ace.fw.mybatis.plus.extension.service;

import ace.fw.data.model.EntityInfo;
import ace.fw.data.model.entity.Entity;
import ace.fw.mybatis.plus.extension.model.EntityServiceInfo;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/6 17:46
 * @description 实体信息服务类
 */
public interface EntityServiceInfoService {
    /**
     * 获取实体关联信息
     *
     * @param id
     * @return
     */
    EntityServiceInfo<Entity> getById(String id);

    /**
     * 获取所有实体信息
     *
     * @return
     */
    List<EntityInfo> getEntityInfos();

    /**
     * 获取对应实体信息，根据实体类型
     *
     * @param entityClassCache
     * @param <T>
     * @return
     */
    <T extends Entity> EntityInfo getByEntityClass(Class<T> entityClassCache);
}
