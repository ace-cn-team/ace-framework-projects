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
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:02
 * @description 通用数据访问层控制器
 */
@Data
@Slf4j
@Validated
public abstract class AbstractController<T, TDbServicePlugin extends DbService<T>, IdType>
        implements AbstractBaseApi<T, IdType> {

    @Autowired
    private TDbServicePlugin dbServicePlugin;

    @Override
    public GenericResponseExt<T> findById(IdType id) {
        return GenericResponseExtUtils.buildSuccessWithData(dbServicePlugin.findById(id));
    }

    @Override
    public GenericResponseExt<List<T>> getListById(List<IdType> ids) {
        List<Object> idList = ids.stream().map(p -> (Object) p).collect(Collectors.toList());
        return GenericResponseExtUtils.buildSuccessWithData(dbServicePlugin.findListById(idList));
    }

    @Override
    public GenericResponseExt<T> findOne(T request) {
        T data = dbServicePlugin.findOne(request);
        GenericResponseExt<T> result = GenericResponseExtUtils.buildSuccessWithData(data);
        return result;
    }

    @Override
    public GenericResponseExt<List<T>> find(FindRequest request) {
        return GenericResponseExtUtils.buildSuccessWithData(dbServicePlugin.find(request));
    }

    @Override
    public GenericResponseExt<Integer> count(WhereRequest request) {
        return GenericResponseExtUtils.buildSuccessWithData(dbServicePlugin.count(request));
    }

    @Override
    public GenericResponseExt<Boolean> save(T entityRequest) {
        return GenericResponseExtUtils.buildSuccessWithData(dbServicePlugin.save(entityRequest));
    }

    @Override
    public GenericResponseExt<Boolean> saveBatch(List<T> entitiesRequest) {
        return GenericResponseExtUtils.buildSuccessWithData(dbServicePlugin.saveBatch(entitiesRequest));
    }

    @Override
    public GenericResponseExt<Boolean> updateById(T request) {
        return GenericResponseExtUtils.buildSuccessWithData(dbServicePlugin.updateById(request));
    }

    @Override
    public GenericResponseExt<Boolean> updateByIdVersionAutoUpdate(T request) {
        return GenericResponseExtUtils.buildSuccessWithData(dbServicePlugin.updateByIdVersionAutoUpdate(request));
    }

    @Override
    public GenericResponseExt<Boolean> updateBatchById(List<T> entitiesRequest) {
        return GenericResponseExtUtils.buildSuccessWithData(dbServicePlugin.updateBatchById(entitiesRequest));
    }

    @Override
    public GenericResponseExt<Boolean> update(EntityUpdateRequest<T> request) {
        return GenericResponseExtUtils.buildSuccessWithData(dbServicePlugin.update(request));
    }

    @Override
    public GenericResponseExt<Boolean> updateForce(EntityUpdateForceRequest<T> request) {
        return GenericResponseExtUtils.buildSuccessWithData(dbServicePlugin.updateForce(request));
    }

    @Override
    public GenericResponseExt<PageResult<T>> page(PageRequest request) {
        return GenericResponseExtUtils.buildSuccessWithData(dbServicePlugin.page(request));
    }

    @Override
    public GenericResponseExt<EntityInfo> getEntityInfo() {
        return GenericResponseExtUtils.buildSuccessWithData(dbServicePlugin.getEntityInfo());
    }


}
