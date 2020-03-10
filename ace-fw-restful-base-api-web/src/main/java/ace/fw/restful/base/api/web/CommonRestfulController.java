package ace.fw.restful.base.api.web;

import ace.fw.data.model.*;
import ace.fw.data.model.entity.Entity;
import ace.fw.data.model.request.resful.*;
import ace.fw.data.model.request.resful.uniform.*;
import ace.fw.data.service.UniformDbService;
import ace.fw.model.response.GenericResponse;
import ace.fw.restful.base.api.service.CommonRestfulBaseService;
import ace.fw.util.GenericResponseUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.*;

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
public class CommonRestfulController implements CommonRestfulBaseService<Entity> {

    private UniformDbService<Entity> uniformDbService;


    public GenericResponse<Entity> getById(@Valid UniformActionRequest<IdRequest> request) {
        return GenericResponseUtils.buildSuccessWithData(uniformDbService.getById(request));
    }

    public GenericResponse<Boolean> save(@Valid UniformActionRequest<UniformSaveRequest> request) {
        return GenericResponseUtils.buildSuccessWithData(uniformDbService.save(request));
    }

    public GenericResponse<Boolean> saveBatch(@Valid UniformActionRequest<UniformSaveBatchRequest> request) {
        return GenericResponseUtils.buildSuccessWithData(uniformDbService.saveBatch(request));
    }


    public GenericResponse<Boolean> updateById(@Valid UniformActionRequest<UniformUpdateByIdRequest> request) {
        return GenericResponseUtils.buildSuccessWithData(uniformDbService.updateById(request));
    }

    public GenericResponse<Boolean> updateBatchById(@Valid UniformActionRequest<UniformUpdateBatchByIdRequest> request) {
        return GenericResponseUtils.buildSuccessWithData(uniformDbService.updateBatchById(request));
    }

    public GenericResponse<Boolean> update(@Valid UniformActionRequest<UniformUpdateRequest> request) {
        return GenericResponseUtils.buildSuccessWithData(uniformDbService.update(request));
    }

    public GenericResponse<Boolean> updateForce(@Valid UniformActionRequest<UniformUpdateForceRequest> request) {
        return GenericResponseUtils.buildSuccessWithData(uniformDbService.updateForce(request));
    }

    public GenericResponse<PageResponse<Entity>> page(@Valid UniformActionRequest<PageQueryRequest> request) {
        return GenericResponseUtils.buildSuccessWithData(uniformDbService.page(request));
    }

    public GenericResponse<List<EntityInfo>> getEntityIds() {
        return GenericResponseUtils.buildSuccessWithData(uniformDbService.getSupportEntityInfo());
    }
}
