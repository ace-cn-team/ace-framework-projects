package ace.fw.data.service;


import ace.fw.data.model.PageResponse;
import ace.fw.data.model.EntityInfo;
import ace.fw.data.model.entity.Entity;
import ace.fw.data.model.request.resful.*;
import ace.fw.data.model.request.resful.entity.EntityUpdateForceRequest;
import ace.fw.data.model.request.resful.entity.EntityUpdateRequest;
import ace.fw.data.model.request.resful.uniform.*;
import ace.fw.model.response.GenericResponseExt;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;


/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/8 10:38
 * @description 泛型服务接口
 */
public interface DbService<T extends Entity> {
    /**
     * 根据ID，获取对象
     *
     * @param id
     * @return
     */
    T getById(Object id);

    /**
     * 根据用户对象实例，查询对旬
     *
     * @param request
     * @return
     */
    T getOne(T request);

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
     * @param entityUpdateRequest
     * @return
     */
    boolean update(EntityUpdateRequest<T> entityUpdateRequest);

    /**
     * 根据条件{@link EntityUpdateForceRequest#getWhere()}与{@link EntityUpdateForceRequest#getEntity()}更新字段(null字段也进行更新)，更新相关对象,
     *
     * @param entityUpdateForceRequest
     * @return
     */
    boolean updateForce(EntityUpdateForceRequest<T> entityUpdateForceRequest);

    /**
     * 根据ID，更新对象,null字段不进行更新,version字段自动更新(输入指定值，自动进行替换)
     * version字段类型支持{@link Integer}{@link Long}
     * version字段类型{@link Integer}指定值{@link Integer#MIN_VALUE)
     * version字段类型{@link Long}指定值{@link Long#MIN_VALUE)
     *
     * @param entity
     * @return
     */
    boolean updateByIdVersionAutoUpdate(T entity);

    /**
     * 分页查询
     *
     * @param queryRequest
     * @return
     */
    PageResponse<T> page(PageQueryRequest queryRequest);

    /**
     * 获取服务相关业务对象元数据
     *
     * @return
     */
    EntityInfo getEntityInfo();

}
