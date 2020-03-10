package ace.fw.mybatis.plus.extension.service.impl;

import ace.fw.data.model.EntityInfo;
import ace.fw.data.model.PageResponse;
import ace.fw.data.model.entity.Entity;
import ace.fw.data.model.request.resful.*;
import ace.fw.data.model.request.resful.entity.EntityUpdateForceRequest;
import ace.fw.data.model.request.resful.entity.EntityUpdateRequest;
import ace.fw.data.model.request.resful.uniform.*;
import ace.fw.exception.BusinessException;
import ace.fw.json.JsonUtils;
import ace.fw.mybatis.plus.extension.enums.SystemDataCodeEnum;
import ace.fw.mybatis.plus.extension.model.EntityServiceInfo;
import ace.fw.mybatis.plus.extension.service.EntityServiceInfoService;
import ace.fw.mybatis.plus.extension.service.MybatisPlusUniformBizService;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/9 11:15
 * @description mybatis plus 通用restful服务实现类
 */
@Data
@Accessors(chain = true)
public class MybatisPlusUniformBizServiceImpl
        implements MybatisPlusUniformBizService<Entity> {

    private EntityServiceInfoService entityServiceInfoService;

    @Override
    public Entity getById(UniformActionRequest<IdRequest> request) {
        EntityServiceInfo<Entity> entityServiceInfo = this.getEntityServiceById(request.getEntityId());

        return entityServiceInfo.getMybatisPlusDbService().getById(request.getEntityId());
    }

    @Override
    public boolean save(UniformActionRequest<UniformSaveRequest> request) {
        EntityServiceInfo<Entity> entityServiceInfo = this.getEntityServiceById(request.getEntityId());

        Entity entity = this.resolveEntityByMap(request.getRequestParam().getEntity(), entityServiceInfo.getEntityClass());

        return ((IService) entityServiceInfo.getMybatisPlusDbService()).save(entity);
    }

    @Override
    public boolean saveBatch(UniformActionRequest<UniformSaveBatchRequest> request) {
        EntityServiceInfo<Entity> entityServiceInfo = this.getEntityServiceById(request.getEntityId());

        List<Entity> entities = this.resolveEntityByMap(request.getRequestParam().getEntities(), entityServiceInfo.getEntityClass());

        return entityServiceInfo.getMybatisPlusDbService().saveBatch(entities);
    }

    @Override
    public boolean updateById(UniformActionRequest<UniformUpdateByIdRequest> request) {
        EntityServiceInfo<Entity> entityServiceInfo = this.getEntityServiceById(request.getEntityId());

        Entity entity = this.resolveEntityByMap(request.getRequestParam().getEntity(), entityServiceInfo.getEntityClass());

        return ((IService) entityServiceInfo.getMybatisPlusDbService()).updateById(entity);
    }

    @Override
    public boolean updateBatchById(UniformActionRequest<UniformUpdateBatchByIdRequest> request) {

        EntityServiceInfo<Entity> entityServiceInfo = this.getEntityServiceById(request.getEntityId());

        List<Entity> entities = this.resolveEntityByMap(request.getRequestParam().getEntities(), entityServiceInfo.getEntityClass());

        return entityServiceInfo.getMybatisPlusDbService().updateBatchById(entities);
    }

    @Override
    public boolean update(UniformActionRequest<UniformUpdateRequest> request) {

        EntityServiceInfo<Entity> entityServiceInfo = this.getEntityServiceById(request.getEntityId());

        Entity entity = this.resolveEntityByMap(request.getRequestParam().getEntity(), entityServiceInfo.getEntityClass());

        EntityUpdateRequest entityUpdateRequest = EntityUpdateRequest.builder()
                .entity(entity)
                .where(request.getRequestParam().getWhere())
                .build();

        return entityServiceInfo.getMybatisPlusDbService().update(entityUpdateRequest);
    }

    @Override
    public boolean updateForce(UniformActionRequest<UniformUpdateForceRequest> request) {
        EntityServiceInfo<Entity> entityServiceInfo = this.getEntityServiceById(request.getEntityId());

        Entity entity = this.resolveEntityByMap(request.getRequestParam().getEntity(), entityServiceInfo.getEntityClass());

        EntityUpdateForceRequest entityEntityUpdateForceRequest = EntityUpdateForceRequest
                .builder()
                .entity(entity)
                .where(request.getRequestParam().getWhere())
                .build();

        return entityServiceInfo.getMybatisPlusDbService().updateForce(entityEntityUpdateForceRequest);

    }

    @Override
    public PageResponse<Entity> page(UniformActionRequest<PageQueryRequest> request) {
        EntityServiceInfo<Entity> entityServiceInfo = this.getEntityServiceById(request.getEntityId());

        return entityServiceInfo.getMybatisPlusDbService().page(request.getRequestParam());
    }

    @Override
    public List<EntityInfo> getSupportEntityInfo() {

        List<EntityInfo> entityInfos = entityServiceInfoService.getEntityInfos();

        return entityInfos;
    }


    private EntityServiceInfo<Entity> getEntityServiceById(String entityId) {
        EntityServiceInfo<Entity> entityServiceInfo = entityServiceInfoService.getById(entityId);

        if (Objects.isNull(entityServiceInfo)) {
            throw new BusinessException(SystemDataCodeEnum.ServiceNotExist.getCode(),
                    SystemDataCodeEnum.ServiceNotExist.getDesc());
        }
        return entityServiceInfo;
    }

    /**
     * 根据实体Map的信息，转换为实体实例
     *
     * @param entityMap
     * @param cls
     * @return
     */
    private <T> T resolveEntityByMap(Map<String, String> entityMap, Class<T> cls) {
        String json = JsonUtils.toJson(entityMap);
        return (T) JsonUtils.toObject(json, cls);
    }

    /**
     * 根据实体Map的信息，转换为实体实例
     *
     * @param entityMaps
     * @param cls
     * @return
     */
    private <T> List<T> resolveEntityByMap(List<Map<String, String>> entityMaps, Class<T> cls) {
        String json = JsonUtils.toJson(entityMaps);
        return JsonUtils.toList(json, cls);
    }
}
