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
 * @description IService封装
 */
public interface DbService<T extends Entity> {
    T getById(Serializable id);

    T getOne(T request);

    boolean save(T entity);

    boolean saveBatch(List<T> entities);

    boolean updateById(T entity);

    boolean updateBatchById(List<T> entities);

    boolean update(EntityUpdateRequest<T> entityUpdateRequest);

    boolean updateForce(EntityUpdateForceRequest<T> entityUpdateForceRequest);

    PageResponse<T> page(PageQueryRequest queryRequest);

    EntityInfo getEntityInfo();

}
