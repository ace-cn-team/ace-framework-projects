package ace.fw.data.service;

import ace.fw.data.model.EntityInfo;
import ace.fw.data.model.PageResponse;
import ace.fw.data.model.entity.Entity;
import ace.fw.data.model.request.resful.*;
import ace.fw.data.model.request.resful.uniform.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/20 11:14
 * @description
 */
public interface UniformDbService<T extends Entity> {
    T getById(@Valid UniformActionRequest<IdRequest> request);

    boolean save(@Valid UniformActionRequest<UniformSaveRequest> request);

    boolean saveBatch(@Valid UniformActionRequest<UniformSaveBatchRequest> request);

    boolean updateById(@Valid UniformActionRequest<UniformUpdateByIdRequest> request);

    boolean updateBatchById(@Valid UniformActionRequest<UniformUpdateBatchByIdRequest> request);

    boolean update(@Valid UniformActionRequest<UniformUpdateRequest> request);

    boolean updateForce(@Valid UniformActionRequest<UniformUpdateForceRequest> request);

    PageResponse<T> page(@Valid UniformActionRequest<PageQueryRequest> request);

    List<EntityInfo> getSupportEntityInfo();
}
