package ace.fw.restful.base.api.service;

import ace.fw.data.model.EntityInfo;
import ace.fw.data.model.PageResponse;
import ace.fw.data.model.request.resful.*;
import ace.fw.data.model.request.resful.uniform.*;
import ace.fw.model.response.GenericResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/14 14:18
 * @description 通用restful服务接口
 */
@Validated
public interface CommonRestfulBaseService<T> {

    @ApiOperation(value = "根据ID获取实体")
    @RequestMapping(path = "/common/entity", method = RequestMethod.GET)
    GenericResponse<T> getById(@Valid @RequestBody UniformActionRequest<IdRequest> request);

    @ApiOperation(value = "新增实体信息")
    @RequestMapping(path = "/common/entity/{entityId}/save", method = RequestMethod.POST)
    GenericResponse<Boolean> save(@Valid @RequestBody UniformActionRequest<UniformSaveRequest> request);

    @ApiOperation(value = "批量新增实体信息")
    @RequestMapping(path = "/common/entity/{entityId}/save-batch", method = RequestMethod.POST)
    GenericResponse<Boolean> saveBatch(@Valid @RequestBody UniformActionRequest<UniformSaveBatchRequest> request);

    @ApiOperation(value = "更新实体信息,根据ID,不更新null值字段")
    @RequestMapping(path = "/common/entity/{entityId}/update-by-id", method = RequestMethod.POST)
    GenericResponse<Boolean> updateById(@Valid @RequestBody UniformActionRequest<UniformUpdateByIdRequest> request);

    @ApiOperation(value = "批量更新实体信息,根据ID,不更新null值字段")
    @RequestMapping(path = "/common/entity/{entityId}/update-batch-by-id", method = RequestMethod.POST)
    GenericResponse<Boolean> updateBatchById(@Valid @RequestBody UniformActionRequest<UniformUpdateBatchByIdRequest> request);

    @ApiOperation(value = "更新实体信息，指定更新列并且指定条件,不更新null值字段")
    @RequestMapping(path = "/common/entity/{entityId}/update", method = RequestMethod.POST)
    GenericResponse<Boolean> update(@Valid @RequestBody UniformActionRequest<UniformUpdateRequest> request);

    @ApiOperation(value = "强制更新实体信息，指定更新列并且指定条件")
    @RequestMapping(path = "/common/entity/{entityId}/update-force", method = RequestMethod.POST)
    GenericResponse<Boolean> updateForce(@Valid @RequestBody UniformActionRequest<UniformUpdateForceRequest> request);

    @ApiOperation(value = "分页查询")
    @RequestMapping(path = "/common/entity/{entityId}/page", method = RequestMethod.GET)
    GenericResponse<PageResponse<T>> page(@Valid @RequestBody UniformActionRequest<PageQueryRequest> request);

    @ApiOperation(value = "获取所有可操作实体")
    @RequestMapping(path = "/common/entities", method = RequestMethod.GET)
    GenericResponse<List<EntityInfo>> getEntityIds();
}
