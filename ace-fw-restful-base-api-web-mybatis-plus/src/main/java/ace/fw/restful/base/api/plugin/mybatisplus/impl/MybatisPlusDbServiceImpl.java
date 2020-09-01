package ace.fw.restful.base.api.plugin.mybatisplus.impl;

import ace.fw.exception.SystemException;
import ace.fw.mybatis.plus.extension.mapper.AceBaseMapper;
import ace.fw.restful.base.api.model.entity.EntityInfo;
import ace.fw.restful.base.api.model.page.PageResult;
import ace.fw.restful.base.api.model.request.base.FindRequest;
import ace.fw.restful.base.api.model.request.base.PageRequest;
import ace.fw.restful.base.api.model.request.base.WhereRequest;
import ace.fw.restful.base.api.model.request.entity.EntityUpdateForceRequest;
import ace.fw.restful.base.api.model.request.entity.EntityUpdateRequest;
import ace.fw.restful.base.api.plugin.EntityMetaDbService;
import ace.fw.restful.base.api.plugin.mybatisplus.MybatisPlusDbService;
import ace.fw.util.ReflectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/27 13:39
 * @description 数据访问层-插件
 */
@Slf4j
public class MybatisPlusDbServiceImpl<T, IdType, Mapper extends AceBaseMapper<T>>
        extends MybatisPlusServiceImpl<T, Mapper>
        implements MybatisPlusDbService<T, IdType> {

    @Autowired
    private Mapper baseMapper;
    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PROTECTED)
    private Integer batchLimitCount = 1000;
    @Autowired
    private EntityMetaDbService entityMetaDbService;
    @Autowired
    private MybatisPlusConverter mybatisPlusConverter;
    private Class entityClassCache;


    public MybatisPlusServiceImpl<T, Mapper> getBaseDbService() {
        return this;
    }

    /**
     * 根据ID，获取对象
     *
     * @param id
     * @return
     */
    @Override
    public T findById(IdType id) {
        return super.getById((Serializable) id);
    }

    @Override
    public List<T> findListById(List<IdType> ids) {
        List<Serializable> serializableList = ids.stream()
                .map(p -> (Serializable) p)
                .collect(Collectors.toList());
        return baseMapper.selectBatchIds(serializableList);
    }

    @Override
    public List<T> find(FindRequest request) {
        QueryWrapper<T> queryWrapper = mybatisPlusConverter.toQueryWrapper(this.getEntityClass(), request);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public T findOne(FindRequest request) {
        QueryWrapper<T> queryWrapper = mybatisPlusConverter.toQueryWrapper(this.getEntityClass(), request);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据用户对象实例，查询对旬
     *
     * @param request
     * @return
     */
    public T findOne(T request) {
        LambdaQueryWrapper<T> lambdaQueryWrapper = Wrappers.lambdaQuery(request);
        return super.getOne(lambdaQueryWrapper);
    }

    /**
     * 保存数据
     *
     * @param entity
     * @return
     */
    public boolean save(T entity) {
        return super.save(entity);
    }

    /**
     * 批量保存数据
     *
     * @param entities
     * @return
     */
    public boolean saveBatch(List<T> entities) {
        return super.saveBatch(entities, batchLimitCount);
    }

    /**
     * 根据ID，更新对象,null字段不进行更新
     *
     * @param entity
     * @return
     */
    public boolean updateById(T entity) {
        return super.updateById(entity);
    }

    /**
     * 批量根据ID，更新对象,null字段不进行更新
     *
     * @param entities
     * @return
     */
    public boolean updateBatchById(List<T> entities) {
        return super.updateBatchById(entities, batchLimitCount);
    }

    /**
     * 根据条件{@link EntityUpdateRequest#getWhere()}与{@link EntityUpdateRequest#getEntity()}更新字段(null字段不进行更新)，更新相关对象,
     *
     * @param request
     * @return
     */
    public boolean update(EntityUpdateRequest<T> request) {
        T entity = request.getEntity();

        UpdateWrapper<T> updateWrapper = mybatisPlusConverter.toUpdateWrapper(this.getEntityClass(), request.getWhere());

        return super.update(entity, updateWrapper);
    }


    /**
     * 根据条件{@link EntityUpdateForceRequest#getWhere()}与{@link EntityUpdateForceRequest#getEntity()}更新字段(null字段也进行更新)，更新相关对象,
     *
     * @param request
     * @return
     */
    public boolean updateForce(EntityUpdateForceRequest<T> request) {

        UpdateWrapper<T> entityUpdateWrapper = mybatisPlusConverter.toForceUpdateWrapper(request.getEntity(), request.getWhere());

        return super.update(entityUpdateWrapper);
    }

    /**
     * 根据ID，更新对象,null字段不进行更新,version字段自动更新(输入指定值，自动进行替换)
     * version字段类型支持{@link Integer}{@link Long}{@link java.util.Date}{@link java.time.LocalDateTime}{@link java.sql.Timestamp}
     *
     * @param entity
     * @return
     */
    public boolean updateByIdVersionAutoUpdate(T entity) {
        return baseMapper.updateByIdVersionAutoUpdate(entity) > 0;
    }

    /**
     * 根据ID，批量更新对象,null字段不进行更新,version字段自动更新(输入指定值，自动进行替换)
     * version字段类型支持{@link Integer}{@link Long}{@link java.util.Date}{@link java.time.LocalDateTime}{@link java.sql.Timestamp}
     *
     * @param entities
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public boolean updateBatchByIdVersionAutoUpdate(List<T> entities) {
        if (entities.size() > batchLimitCount) {
            throw new IllegalArgumentException("一次执行数量最多：" + batchLimitCount);
        }
        return baseMapper.updateBatchByIdVersionAutoUpdate(entities) > 0;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public PageResult<T> page(PageRequest request) {
        Pair<Page<T>, QueryWrapper<T>> pair = mybatisPlusConverter.toPageWrapper(this.getEntityClass(), request);
        QueryWrapper<T> queryWrapper = pair.getRight();
        Page mybatisPlusPage = pair.getLeft();

        IPage<T> mybatisPageResult = super.page(mybatisPlusPage, queryWrapper);

        PageResult<T> pageResult = mybatisPlusConverter.buildPageResultFrom(mybatisPageResult);

        return pageResult;
    }

    @Override
    public EntityInfo getEntityInfo() {
        return entityMetaDbService.getEntityInfo(this.getEntityClass());
    }

    @Override
    public Integer count(WhereRequest request) {
        QueryWrapper queryWrapper = mybatisPlusConverter.toQueryWrapper(this.getEntityClass(), FindRequest.builder().where(request).build());
        return baseMapper.selectCount(queryWrapper);
    }


    /**
     * 获取当前实体类型
     *
     * @return
     */
    protected Class<T> getEntityClass() {
        if (Objects.nonNull(entityClassCache)) {
            return entityClassCache;
        }

        entityClassCache = (Class<T>) ReflectionUtils.getClassGeneric(this.getClass(), 0);
        if (Objects.isNull(entityClassCache)) {
            throw new SystemException(this.getClass().getName() + ",查询entity class失败");
        }

        return entityClassCache;
    }
}
