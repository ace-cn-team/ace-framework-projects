package ace.fw.mybatis.plus.extension.service.impl;

import ace.fw.data.model.*;
import ace.fw.data.model.entity.Entity;
import ace.fw.data.model.request.QueryRequest;
import ace.fw.data.model.request.resful.*;
import ace.fw.data.model.request.resful.entity.EntityUpdateForceRequest;
import ace.fw.data.model.request.resful.entity.EntityUpdateRequest;
import ace.fw.exception.SystemException;
import ace.fw.mybatis.plus.extension.service.EntityConfigInfoService;
import ace.fw.mybatis.plus.extension.service.MybatisPlusDbService;
import ace.fw.mybatis.plus.extension.util.MybatisPlusUtils;
import ace.fw.util.ReflectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/8 10:40
 * @description 数据层抽象逻辑实现
 */
public class MybatisPlusDbServiceImpl<T extends Entity, Mapper extends BaseMapper<T>>
        extends ServiceImpl<Mapper, T>
        implements MybatisPlusDbService<T> {
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    @Autowired
    private EntityConfigInfoService entityConfigInfoService;

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private Class<T> entityClassCache;
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private Object entityClassCacheLock = new Object();
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private Integer batchLimitCount = 1000;


    @Override
    public T getOne(T request) {
        LambdaQueryWrapper<T> lambdaQueryWrapper = Wrappers.lambdaQuery(request);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean saveBatch(List<T> entities) {
        return this.saveBatch(entities, batchLimitCount);
    }

    @Override
    public boolean updateBatchById(List<T> entities) {
        return this.updateBatchById(entities, batchLimitCount);
    }

    @Override
    public boolean update(EntityUpdateRequest<T> entityUpdateRequest) {
        T entity = entityUpdateRequest.getEntity();

        UpdateWrapper<T> entityUpdateWrapper = Wrappers.update();

        MybatisPlusUtils.addWhereToWrapper(entityUpdateWrapper,
                this.getEntityInfo(),
                entityUpdateRequest.getWhere()
        );
        return this.update(entity, entityUpdateWrapper);
    }

    @Override
    public boolean updateForce(EntityUpdateForceRequest<T> entityUpdateForceRequest) {
        T entity = entityUpdateForceRequest.getEntity();

        EntityInfo entityInfo = this.getEntityInfo();

        List<String> updateProperties = this.resolveUpdateProperties(entityInfo);

        UpdateWrapper<T> entityUpdateWrapper = Wrappers.update();

        MybatisPlusUtils.addSetToUpdateWrapper(entityUpdateWrapper,
                entityInfo,
                entity,
                updateProperties
        );

        MybatisPlusUtils.addWhereToWrapper(entityUpdateWrapper,
                entityInfo,
                entityUpdateForceRequest.getWhere()
        );

        return this.update(entityUpdateWrapper);
    }

    @Override
    public PageResponse<T> page(PageQueryRequest pageQueryRequest) {
        QueryRequest queryRequest = this.toQueryRequest(pageQueryRequest);

        QueryWrapper<T> queryWrapper = Wrappers.query();

        EntityInfo entityInfo = this.getEntityInfo();

        MybatisPlusUtils.addSelectToQueryWrapper(
                queryWrapper,
                entityInfo,
                queryRequest.getSelect());

        MybatisPlusUtils.addWhereToWrapper(
                queryWrapper,
                entityInfo,
                queryRequest.getWhere());

        Page<T> pageDbRequest = MybatisPlusUtils.buildPageFrom(
                entityInfo,
                queryRequest.getPage(),
                queryRequest.getOrderBy());

        IPage<T> pageResult = this.page(pageDbRequest, queryWrapper);

        PageResponse<T> pageResponse = MybatisPlusUtils.buildPageResponseFrom(pageResult);

        return pageResponse;
    }

    private QueryRequest toQueryRequest(PageQueryRequest pageQueryRequest) {
        QueryRequest queryRequest = new QueryRequest();
        if (Objects.nonNull(pageQueryRequest)) {
            if (CollectionUtils.isNotEmpty(pageQueryRequest.getFields())) {
                queryRequest.setSelect(new Select().setFields(pageQueryRequest.getFields()));
            }

            if (CollectionUtils.isNotEmpty(pageQueryRequest.getConditions())) {
                WhereRequest whereRequest = new WhereRequest();
                whereRequest.setConditions(pageQueryRequest.getConditions());
                queryRequest.setWhere(whereRequest);
            }

            PageRequest pageRequest = new PageRequest()
                    .setPageIndex(pageQueryRequest.getPageIndex() == null ? 0 : pageQueryRequest.getPageIndex())
                    .setPageSize(pageQueryRequest.getPageSize() == null ? 10 : pageQueryRequest.getPageSize());
            queryRequest.setPage(pageRequest);

            if (CollectionUtils.isNotEmpty(pageQueryRequest.getSorts())) {
                OrderBy orderBy = new OrderBy()
                        .setSorts(pageQueryRequest.getSorts());
                queryRequest.setOrderBy(orderBy);
            }
        }
        return queryRequest;
    }

    @Override
    public EntityInfo getEntityInfo() {
        return entityConfigInfoService.getByEntityClass(getEntityClass());
    }

    protected Class<T> getEntityClass() {
        if (Objects.nonNull(entityClassCache)) {
            return entityClassCache;
        }
        synchronized (entityClassCacheLock) {
            entityClassCache = (Class<T>) ReflectionUtils.getClassGeneric(this.getClass(), 0);
            if (Objects.isNull(entityClassCache)) {
                throw new SystemException(this.getClass().getName() + ",查询entity class失败");
            }
        }
        return entityClassCache;
    }

    private List<String> resolveUpdateProperties(EntityInfo entityInfo) {
        return entityInfo.getProperties()
                .stream()
                .map(p -> p.getId())
                .collect(Collectors.toList());
    }
}
