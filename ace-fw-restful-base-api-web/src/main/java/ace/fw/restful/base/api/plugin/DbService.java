package ace.fw.restful.base.api.plugin;

import ace.fw.restful.base.api.model.entity.EntityInfo;
import ace.fw.restful.base.api.model.page.PageResult;
import ace.fw.restful.base.api.model.request.base.FindRequest;
import ace.fw.restful.base.api.model.request.base.PageRequest;
import ace.fw.restful.base.api.model.request.base.SelectRequest;
import ace.fw.restful.base.api.model.request.base.WhereRequest;
import ace.fw.restful.base.api.model.request.entity.EntityUpdateForceRequest;
import ace.fw.restful.base.api.model.request.entity.EntityUpdateRequest;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/27 13:39
 * @description 数据访问层-插件
 */
public interface DbService<T, IdType> {

    /**
     * 根据ID，获取对象
     *
     * @param id
     * @return
     */
    T findById(IdType id);

    /**
     * 根据ID，获取对象
     *
     * @param ids
     * @return
     */
    List<T> findListById(List<IdType> ids);

    /**
     * 查询数据
     *
     * @param request
     * @return
     */
    List<T> find(FindRequest request);

    /**
     * 查询数据，返回一条
     *
     * @param request
     * @return
     */
    T findOne(FindRequest request);

    /**
     * 根据用户对象实例，查询对旬
     *
     * @param request
     * @return
     */
    T findOne(T request);


    /**
     * 保存数据
     *
     * @param entity
     * @return
     */
    boolean save(T entity);

    /**
     * 批量保存数据
     *
     * @param entities
     * @return
     */
    boolean saveBatch(List<T> entities);

    /**
     * 根据ID，更新对象,null字段不进行更新
     *
     * @param entity
     * @return
     */
    boolean updateById(T entity);

    /**
     * 批量根据ID，更新对象,null字段不进行更新
     *
     * @param entities
     * @return
     */
    boolean updateBatchById(List<T> entities);

    /**
     * 根据条件{@link EntityUpdateRequest#getWhere()}与{@link EntityUpdateRequest#getEntity()}更新字段(null字段不进行更新)，更新相关对象,
     *
     * @param request
     * @return
     */
    boolean update(EntityUpdateRequest<T> request);

    /**
     * 根据条件{@link EntityUpdateForceRequest#getWhere()}与{@link EntityUpdateForceRequest#getEntity()}更新字段(null字段也进行更新)，更新相关对象,
     *
     * @param entityUpdateForceRequest
     * @return
     */
    boolean updateForce(EntityUpdateForceRequest<T> entityUpdateForceRequest);

    /**
     * 根据ID，更新对象,null字段不进行更新,version字段自动更新(输入指定值，自动进行替换)
     * version字段类型支持{@link Integer}{@link Long}{@link java.util.Date}{@link java.time.LocalDateTime}{@link java.sql.Timestamp}
     *
     * @param entity
     * @return
     */
    boolean updateByIdVersionAutoUpdate(T entity);

    /**
     * 根据ID，批量更新对象,null字段不进行更新,version字段自动更新(输入指定值，自动进行替换)
     * version字段类型支持{@link Integer}{@link Long}{@link java.util.Date}{@link java.time.LocalDateTime}{@link java.sql.Timestamp}
     *
     * @param entities
     * @return
     */
    boolean updateBatchByIdVersionAutoUpdate(List<T> entities);

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    PageResult<T> page(PageRequest request);

    /**
     * 获取当前实体类型的映射信息
     *
     * @return
     */
    EntityInfo getEntityInfo();

    /**
     * 统计数量
     *
     * @param request
     * @return
     */
    Integer count(WhereRequest request);
}
