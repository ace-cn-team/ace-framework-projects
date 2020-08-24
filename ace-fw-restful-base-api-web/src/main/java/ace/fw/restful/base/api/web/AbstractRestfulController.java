package ace.fw.restful.base.api.web;

import ace.fw.data.model.EntityInfo;
import ace.fw.data.model.PageResponse;
import ace.fw.data.model.entity.Entity;
import ace.fw.data.model.request.resful.*;
import ace.fw.data.model.request.resful.entity.EntityUpdateForceRequest;
import ace.fw.data.model.request.resful.entity.EntityUpdateRequest;

import ace.fw.data.service.DbService;
import ace.fw.model.response.GenericResponseExt;
import ace.fw.restful.base.api.service.AbstractRestfulBaseService;
import ace.fw.util.GenericResponseExtUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:02
 * @description 通用数据访问层控制器
 */
@Data
@Accessors(chain = true)
@Slf4j
@Validated
public abstract class AbstractRestfulController<T extends Entity, S extends DbService<T>>
        implements AbstractRestfulBaseService<T> {

    @Autowired
    private S dbService;


    @Override
    public GenericResponseExt<T> getById(String id) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.getById(id));
    }

    @Override
    public GenericResponseExt<T> getOne(T request) {
        T data = dbService.getOne(request);
        GenericResponseExt<T> result = GenericResponseExtUtils.buildSuccessWithData(data);
        return result;
    }

    @Override
    public GenericResponseExt<Boolean> save(T entityRequest) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.save(entityRequest));
    }

    @Override
    public GenericResponseExt<Boolean> saveBatch(List<T> entitiesRequest) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.saveBatch(entitiesRequest));
    }

    @Override
    public GenericResponseExt<Boolean> updateById(T entityRequest) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.updateById(entityRequest));
    }

    @Override
    public GenericResponseExt<Boolean> updateByIdVersionAutoUpdate(T entityRequest) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.updateByIdVersionAutoUpdate(entityRequest));
    }

    @Override
    public GenericResponseExt<Boolean> updateBatchById(List<T> entitiesRequest) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.updateBatchById(entitiesRequest));
    }

    @Override
    public GenericResponseExt<Boolean> update(EntityUpdateRequest<T> entityUpdateRequest) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.update(entityUpdateRequest));
    }

    @Override
    public GenericResponseExt<Boolean> updateForce(@Valid EntityUpdateForceRequest<T> entityUpdateForceRequest) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.updateForce(entityUpdateForceRequest));
    }

    @Override
    public GenericResponseExt<PageResponse<T>> page(PageQueryRequest queryRequest) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.page(queryRequest));
    }

    @Override
    public GenericResponseExt<EntityInfo> getEntityInfo() {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.getEntityInfo());
    }
}
