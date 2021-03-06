package ace.fw.restful.base.api;

import ace.fw.restful.base.api.model.entity.EntityInfo;
import ace.fw.restful.base.api.model.page.PageResult;
import ace.fw.restful.base.api.model.request.base.FindRequest;
import ace.fw.restful.base.api.model.request.base.PageRequest;
import ace.fw.model.response.GenericResponseExt;
import ace.fw.restful.base.api.model.request.base.WhereRequest;
import ace.fw.restful.base.api.model.request.entity.EntityUpdateForceRequest;
import ace.fw.restful.base.api.model.request.entity.EntityUpdateRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/16 18:48
 * @description 抽象restful服务接口
 * RequestMapping path = 实体对象名称
 */
@Validated
public interface AbstractBaseApi<T, IdType> {

    @ApiOperation(value = "根据ID获取实体")
    @RequestMapping(path = "/find-by-id", method = RequestMethod.POST)
    GenericResponseExt<T> findById(@NotNull(message = "id不能为空") @RequestBody IdType id);

    @ApiOperation(value = "根据ID获取实体")
    @RequestMapping(path = "/find-list-by-id", method = RequestMethod.POST)
    GenericResponseExt<List<T>> getListById(@NotNull(message = "id不能为空") @Size(min = 1) @RequestBody List<IdType> ids);

    @ApiOperation(value = "查询一条记录")
    @RequestMapping(path = "/find-one", method = RequestMethod.POST)
    GenericResponseExt<T> findOne(@NotNull @RequestBody T request);

    @ApiOperation(value = "查询")
    @RequestMapping(path = "/find", method = RequestMethod.POST)
    GenericResponseExt<List<T>> find(@NotNull @Valid @RequestBody FindRequest request);

    @ApiOperation(value = "统计数量")
    @RequestMapping(path = "/count", method = RequestMethod.POST)
    GenericResponseExt<Integer> count(@RequestBody WhereRequest request);

    @ApiOperation(value = "新增实体信息")
    @RequestMapping(path = "/save", method = RequestMethod.POST)
    GenericResponseExt<Boolean> save(@NotNull @Valid @RequestBody T entityRequest);

    @ApiOperation(value = "批量新增实体信息")
    @RequestMapping(path = "/save-batch", method = RequestMethod.POST)
    GenericResponseExt<Boolean> saveBatch(@NotNull @Size(min = 1) @Valid @RequestBody List<T> request);

    @ApiOperation(value = "更新实体信息,根据ID,不更新null值字段")
    @RequestMapping(path = "/update-by-id", method = RequestMethod.POST)
    GenericResponseExt<Boolean> updateById(@NotNull @RequestBody T request);

    @ApiOperation(value = "更新实体信息,根据ID,不更新null值字段,version字段自动更新")
    @RequestMapping(path = "/update-by-id-version-auto-update", method = RequestMethod.POST)
    GenericResponseExt<Boolean> updateByIdVersionAutoUpdate(@NotNull @RequestBody T request);

    @ApiOperation(value = "批量更新实体信息,根据ID,不更新null值字段")
    @RequestMapping(path = "/update-batch-by-id", method = RequestMethod.POST)
    GenericResponseExt<Boolean> updateBatchById(@NotNull @Size(min = 1) @RequestBody List<T> request);

    @ApiOperation(value = "更新实体信息，指定更新列并且指定条件,不更新null值字段")
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    GenericResponseExt<Boolean> update(@NotNull @Valid @RequestBody EntityUpdateRequest<T> request);

    @ApiOperation(value = "强制更新实体信息，指定更新列并且指定条件")
    @RequestMapping(path = "/update-force", method = RequestMethod.POST)
    GenericResponseExt<Boolean> updateForce(@NotNull @Valid @RequestBody EntityUpdateForceRequest<T> request);

    @ApiOperation(value = "分页查询")
    @RequestMapping(path = "/page", method = RequestMethod.POST)
    GenericResponseExt<PageResult<T>> page(@NotNull @Valid @RequestBody PageRequest request);

    @ApiOperation(value = "获取对应实体信息")
    @RequestMapping(path = "/entity-info", method = RequestMethod.POST)
    GenericResponseExt<EntityInfo> getEntityInfo();
}
