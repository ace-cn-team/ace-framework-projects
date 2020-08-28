package ace.fw.restful.base.api.plugin.mybatisplus.impl;

import ace.fw.exception.SystemException;
import ace.fw.mybatis.plus.extension.mapper.AceBaseMapper;
import ace.fw.restful.base.api.enums.LogicalOpEnum;
import ace.fw.restful.base.api.enums.RelationalOpEnum;
import ace.fw.restful.base.api.model.*;
import ace.fw.restful.base.api.model.orderby.OrderBy;
import ace.fw.restful.base.api.model.request.PageRequest;
import ace.fw.restful.base.api.model.request.entity.EntityUpdateForceRequest;
import ace.fw.restful.base.api.model.request.entity.EntityUpdateRequest;
import ace.fw.restful.base.api.model.select.Select;
import ace.fw.restful.base.api.model.where.Where;
import ace.fw.restful.base.api.plugin.EntityMetaDbService;
import ace.fw.restful.base.api.plugin.DbService;
import ace.fw.util.ReflectionUtils;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
public class DbServiceImpl<Mapper extends AceBaseMapper<T>, T>
        extends ServiceImpl<Mapper, T>
        implements DbService<T> {

    @Autowired
    private Mapper mapper;
    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PROTECTED)
    private Integer batchLimitCount = 1000;
    @Autowired
    private EntityMetaDbService entityMetaDbService;

    private Class entityClassCache;

    /**
     * 根据ID，获取对象
     *
     * @param id
     * @return
     */
    public T getById(Object id) {
        return super.getById((Serializable) id);
    }

    @Override
    public List<T> getListById(List<Object> ids) {
        List<Serializable> serializableList = ids.stream()
                .map(p -> (Serializable) p)
                .collect(Collectors.toList());
        return baseMapper.selectBatchIds(serializableList);
    }

    /**
     * 根据用户对象实例，查询对旬
     *
     * @param request
     * @return
     */
    public T getOne(T request) {
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

        UpdateWrapper<T> entityUpdateWrapper = Wrappers.update();

        this.addWhereToWrapper(entityUpdateWrapper, request.getWhere());

        return super.update(entity, entityUpdateWrapper);
    }


    /**
     * 根据条件{@link EntityUpdateForceRequest#getWhere()}与{@link EntityUpdateForceRequest#getEntity()}更新字段(null字段也进行更新)，更新相关对象,
     *
     * @param request
     * @return
     */
    public boolean updateForce(EntityUpdateForceRequest<T> request) {

        List<String> updateProperties = this.getEntityProperties(this.getEntityInfo());

        UpdateWrapper<T> entityUpdateWrapper = Wrappers.update();

        this.addSetToUpdateWrapper(entityUpdateWrapper, request.getEntity(), updateProperties);

        this.addWhereToWrapper(entityUpdateWrapper, request.getWhere());

        return super.update(entityUpdateWrapper);
    }

    /**
     * 根据ID，更新对象,null字段不进行更新,version字段自动更新(输入指定值，自动进行替换)
     * version字段类型支持{@link Integer}{@link Long}
     * version字段类型{@link Integer}指定值{@link Integer#MIN_VALUE)
     * version字段类型{@link Long}指定值{@link Long#MIN_VALUE)
     *
     * @param entity
     * @return
     */
    public boolean updateByIdVersionAutoUpdate(T entity) {
        return mapper.updateByIdVersionAutoUpdate(entity) > 0;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public PageResult<T> page(PageRequest request) {

        QueryWrapper<T> queryWrapper = Wrappers.query();

        this.addSelectToQueryWrapper(queryWrapper, request.getSelect());

        this.addWhereToWrapper(queryWrapper, request.getWhere());

        com.baomidou.mybatisplus.extension.plugins.pagination.Page mybatisPlusPage = this.buildPageFrom(request.getPage(), request.getOrderBy());

        IPage<T> mybatisPageResult = super.page(mybatisPlusPage, queryWrapper);

        PageResult<T> pageResult = this.buildPageResultFrom(mybatisPageResult);

        return pageResult;
    }


    /**
     * 转换where 条件,转换不为null的字段，并自动按entity对象字段类型进行转换，最后添加到UpdateWrapper
     *
     * @param wrapper 实体更新操作实例
     * @param where   Where条件
     * @param <T>     实体类型
     * @return
     */
    public <T, Children extends AbstractWrapper<T, String, Children>, ConditionT>
    void addWhereToWrapper(AbstractWrapper<T, String, Children> wrapper, Where where) {
        if (Objects.isNull(where) || CollectionUtils.isEmpty(where.getConditions())) {
            return;
        }
        List<Condition> conditions = where.getConditions();
        conditions
                .stream()
                .forEach(condition -> {
                    addConditionToWrapper(wrapper, condition);
                });
    }

    /**
     * 添加condition到UpdateWrapper对象
     *
     * @param wrapper      需要添加的UpdateWrapper对象
     * @param condition    准备添加的条件
     * @param <T>          实体类型
     * @param <ConditionT> 条件的泛型值
     * @return
     */
    public <T, Children extends AbstractWrapper<T, String, Children>, ConditionT> void
    addConditionToWrapper(AbstractWrapper<T, String, Children> wrapper,
                          Condition condition) {
        String columnName = this.entityMetaDbService.getColumnName(this.getEntityClass(), condition.getPropertyName());

        if (LogicalOpEnum.OR.equals(condition.getLogicalOp())) {
            wrapper.or();
        }
        if (RelationalOpEnum.EQ.equals(condition.getRelationalOp())) {
            wrapper.eq(Objects.nonNull(condition.getValue()), columnName, condition.getValue());
        } else if (RelationalOpEnum.NE.equals(condition.getRelationalOp())) {
            wrapper.ne(Objects.nonNull(condition.getValue()), columnName, condition.getValue());
        } else if (RelationalOpEnum.IN.equals(condition.getRelationalOp())) {
            wrapper.in(CollectionUtils.isNotEmpty(condition.getValues()), columnName, condition.getValues());
        } else if (RelationalOpEnum.LIKE.equals(condition.getRelationalOp())) {
            wrapper.like(Objects.nonNull(condition.getValue()), columnName, condition.getValue());
        } else if (RelationalOpEnum.LIKE_LEFT.equals(condition.getRelationalOp())) {
            wrapper.likeLeft(Objects.nonNull(condition.getValue()), columnName, condition.getValue());
        } else if (RelationalOpEnum.LIKE_RIGHT.equals(condition.getRelationalOp())) {
            wrapper.likeRight(Objects.nonNull(condition.getValue()), columnName, condition.getValue());
        } else if (RelationalOpEnum.GT.equals(condition.getRelationalOp())) {
            wrapper.gt(Objects.nonNull(condition.getValue()), columnName, condition.getValue());
        } else if (RelationalOpEnum.GE.equals(condition.getRelationalOp())) {
            wrapper.ge(Objects.nonNull(condition.getValue()), columnName, condition.getValue());
        } else if (RelationalOpEnum.LT.equals(condition.getRelationalOp())) {
            wrapper.lt(Objects.nonNull(condition.getValue()), columnName, condition.getValue());
        } else if (RelationalOpEnum.LE.equals(condition.getRelationalOp())) {
            wrapper.le(Objects.nonNull(condition.getValue()), columnName, condition.getValue());
        } else if (RelationalOpEnum.IS_NULL.equals(condition.getRelationalOp())) {
            wrapper.isNull(true, columnName);
        } else if (RelationalOpEnum.IS_NOT_NULL.equals(condition.getRelationalOp())) {
            wrapper.isNotNull(true, columnName);
        } else if (RelationalOpEnum.BETWEEN.equals(condition.getRelationalOp())) {
            wrapper.between(CollectionUtils.isNotEmpty(condition.getValues()) && condition.getValues().size() > 1, columnName, condition.getValues().get(0), condition.getValues().get(1));
        } else if (RelationalOpEnum.NOT_BETWEEN.equals(condition.getRelationalOp())) {
            wrapper.notBetween(CollectionUtils.isNotEmpty(condition.getValues()) && condition.getValues().size() > 1, columnName, condition.getValues().get(0), condition.getValues().get(1));
        } else if (RelationalOpEnum.NOT_IN.equals(condition.getRelationalOp())) {
            wrapper.notIn(CollectionUtils.isNotEmpty(condition.getValues()), columnName, condition.getValues());
        } else if (RelationalOpEnum.NOT_LIKE.equals(condition.getRelationalOp())) {
            wrapper.notLike(Objects.nonNull(condition.getValue()), columnName, condition.getValue());
        } else {
            throw new RuntimeException("不支持的RelationalOp操作," + condition.getRelationalOp().getDesc());
        }
    }

    /**
     * 创建Mybatis plus 分页对象
     *
     * @param page
     * @param orderBy
     * @return
     */
    private com.baomidou.mybatisplus.extension.plugins.pagination.Page buildPageFrom(Page page, OrderBy orderBy) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page mybatisPlusPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page();
        mybatisPlusPage.setCurrent(page.getPageIndex());
        mybatisPlusPage.setSize(page.getPageSize());

        if (Objects.isNull(orderBy) || CollectionUtils.isEmpty(orderBy.getSorts())) {
            return mybatisPlusPage;
        }
        List<Sort> sorts = orderBy.getSorts();
        List<OrderItem> orderItems = sorts
                .stream()
                .map(p -> {
                            String columnName = entityMetaDbService.getColumnName(this.getEntityClass(), p.getProperty());
                            return new OrderItem()
                                    .setAsc(p.getAsc())
                                    .setColumn(columnName);
                        }
                )
                .collect(Collectors.toList());

        mybatisPlusPage.addOrder(orderItems);

        return mybatisPlusPage;
    }

    /**
     * 创建restful 分页对象
     *
     * @param mybatisPlusPageResult
     * @return
     */
    public <T> PageResult<T> buildPageResultFrom(IPage<T> mybatisPlusPageResult) {
        PageResult<T> pageResult = PageResult.<T>builder()
                .data(mybatisPlusPageResult.getRecords())
                .totalCount(mybatisPlusPageResult.getTotal())
                .totalPage(mybatisPlusPageResult.getPages())
                .build();

        return pageResult;
    }

    /**
     * 创建UpdateWrapper
     * 根据指定属性进行强制更新，含null的属性
     *
     * @param updateWrapper    实体更新操作实例
     * @param entity           准备更新的实体实例
     * @param updateProperties 指定更新的属性
     * @return
     */
    public <M> void addSetToUpdateWrapper(UpdateWrapper<M> updateWrapper, M entity, List<String> updateProperties) {
        EntityInfo entityInfo = this.getEntityInfo();
        // 转换set 的值与列
        entityInfo
                .getProperties()
                .stream()
                .filter(entityProperty -> updateProperties.contains(entityProperty.getId()))
                .forEach(entityProperty -> {
                    try {
                        Object value = FieldUtils.readDeclaredField(entity, entityProperty.getId(), true);
                        updateWrapper.set(true, entityProperty.getColumn(), value);
                    } catch (IllegalAccessException e) {
                        String msg = String.format("%s实体无法读取%s属性", entityInfo.getId(), entityProperty.getId());
                        throw new SystemException(msg, e);
                    }
                });
    }

    @Override
    public EntityInfo getEntityInfo() {
        return entityMetaDbService.getEntityInfo(this.getEntityClass());
    }

    /**
     * 添加Select的字段到QueryWrapper
     *
     * @param queryWrapper
     * @param select
     * @param <T>
     */
    public <T> void addSelectToQueryWrapper(QueryWrapper<T> queryWrapper, Select select) {
        if (Objects.isNull(select) || CollectionUtils.isEmpty(select.getProperties())) {
            return;
        }
        List<String> properties = select.getProperties();
        List<String> columns = properties
                .stream()
                .map(p -> getEntityProperty(p))
                .filter(p -> p != null)
                .map(p -> p.getColumn())
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(columns)) {
            return;
        }

        queryWrapper.select(columns.toArray(new String[columns.size()]));
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

        entityClassCache = (Class<T>) ReflectionUtils.getClassGeneric(this.getClass(), 1);
        if (Objects.isNull(entityClassCache)) {
            throw new SystemException(this.getClass().getName() + ",查询entity class失败");
        }

        return entityClassCache;
    }

    private List<String> getEntityProperties(EntityInfo entityInfo) {
        return entityInfo.getProperties()
                .stream()
                .map(p -> p.getId())
                .collect(Collectors.toList());
    }

    public EntityProperty getEntityProperty(String property) {
        return this.getEntityInfo().getProperties()
                .stream()
                .filter(entityProperty -> StringUtils.equals(entityProperty.getId(), property))
                .findFirst()
                .orElse(null);
    }
}
