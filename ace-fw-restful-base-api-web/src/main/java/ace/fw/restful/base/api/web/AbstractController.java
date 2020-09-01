package ace.fw.restful.base.api.web;


import ace.fw.restful.base.api.model.entity.EntityInfo;
import ace.fw.restful.base.api.model.page.PageResult;
import ace.fw.restful.base.api.model.request.base.FindRequest;
import ace.fw.restful.base.api.model.request.base.PageRequest;

import ace.fw.model.response.GenericResponseExt;
import ace.fw.restful.base.api.AbstractBaseApi;
import ace.fw.restful.base.api.model.request.base.WhereRequest;
import ace.fw.restful.base.api.model.request.entity.EntityUpdateForceRequest;
import ace.fw.restful.base.api.model.request.entity.EntityUpdateRequest;
import ace.fw.restful.base.api.plugin.DbService;
import ace.fw.util.GenericResponseExtUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:02
 * @description 通用数据访问层控制器
 */
@Slf4j
@Validated
public abstract class AbstractController<T, IdType, TDbService extends DbService<T, IdType>>
        implements AbstractBaseApi<T, IdType> {

    @Autowired
    private TDbService dbService;

    protected TDbService getDbService() {
        return dbService;
    }

    @Override

    public GenericResponseExt<T> findById(IdType id) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.findById(id));
    }

    @Override
    public GenericResponseExt<List<T>> getListById(List<IdType> ids) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.findListById(ids));
    }

    @Override
    public GenericResponseExt<T> findOne(T request) {
        T data = dbService.findOne(request);
        GenericResponseExt<T> result = GenericResponseExtUtils.buildSuccessWithData(data);
        return result;
    }

    @Override
    public GenericResponseExt<List<T>> find(FindRequest request) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.find(request));
    }

    @Override
    public GenericResponseExt<Integer> count(WhereRequest request) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.count(request));
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
    public GenericResponseExt<Boolean> updateById(T request) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.updateById(request));
    }

    @Override
    public GenericResponseExt<Boolean> updateByIdVersionAutoUpdate(T request) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.updateByIdVersionAutoUpdate(request));
    }

    @Override
    public GenericResponseExt<Boolean> updateBatchById(List<T> entitiesRequest) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.updateBatchById(entitiesRequest));
    }

    @Override
    public GenericResponseExt<Boolean> update(EntityUpdateRequest<T> request) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.update(request));
    }

    @Override
    public GenericResponseExt<Boolean> updateForce(EntityUpdateForceRequest<T> request) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.updateForce(request));
    }

    @Override
    public GenericResponseExt<PageResult<T>> page(PageRequest request) {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.page(request));
    }

    @Override
    public GenericResponseExt<EntityInfo> getEntityInfo() {
        return GenericResponseExtUtils.buildSuccessWithData(dbService.getEntityInfo());
    }


}
