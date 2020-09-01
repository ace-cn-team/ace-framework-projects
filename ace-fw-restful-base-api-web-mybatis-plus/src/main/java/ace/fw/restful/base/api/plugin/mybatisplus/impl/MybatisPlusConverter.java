package ace.fw.restful.base.api.plugin.mybatisplus.impl;

import ace.fw.exception.SystemException;
import ace.fw.restful.base.api.enums.LogicalOpEnum;
import ace.fw.restful.base.api.enums.RelationalOpEnum;
import ace.fw.restful.base.api.model.entity.EntityInfo;
import ace.fw.restful.base.api.model.entity.EntityProperty;
import ace.fw.restful.base.api.model.orderby.OrderBy;
import ace.fw.restful.base.api.model.orderby.Sort;
import ace.fw.restful.base.api.model.page.PageResult;
import ace.fw.restful.base.api.model.request.base.*;
import ace.fw.restful.base.api.model.request.entity.EntityUpdateRequest;
import ace.fw.restful.base.api.model.select.Select;
import ace.fw.restful.base.api.model.where.Condition;
import ace.fw.restful.base.api.model.where.Where;
import ace.fw.restful.base.api.plugin.EntityMetaDbService;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/31 11:06
 * @description restful DbService层的对象与mybatisplus的对象转换器
 */
@Slf4j
public class MybatisPlusConverter {
    @Autowired
    private EntityMetaDbService entityMetaDbService;

    public <T> QueryWrapper<T> toQueryWrapper(Class<T> tClass, FindRequest request) {
        QueryWrapper<T> queryWrapper = Wrappers.query();
        this.addSelectToQueryWrapper(tClass, queryWrapper, request.getSelect());
        this.addWhereToWrapper(tClass, queryWrapper, request.getWhere());
        this.addOrderByToWrapper(tClass, queryWrapper, request.getOrderBy());
        return queryWrapper;
    }

    public <T> Pair<Page<T>, QueryWrapper<T>> toPageWrapper(Class<T> tClass, PageRequest request) {
        QueryWrapper<T> queryWrapper = Wrappers.query();
        this.addSelectToQueryWrapper(tClass, queryWrapper, request.getSelect());
        this.addWhereToWrapper(tClass, queryWrapper, request.getWhere());
        this.addOrderByToWrapper(tClass, queryWrapper, request.getOrderBy());
        Page mybatisPlusPage = this.buildPageFrom(tClass, request.getPager(), request.getOrderBy());
        return Pair.of(mybatisPlusPage, queryWrapper);
    }

    public <T> UpdateWrapper<T> toUpdateWrapper(Class<T> tClass, Where where) {
        UpdateWrapper<T> updateWrapper = Wrappers.update();

        this.addWhereToWrapper(tClass, updateWrapper, where);

        return updateWrapper;
    }

    public <T> UpdateWrapper<T> toForceUpdateWrapper(T entity, Where where) {
        List<String> updateProperties = this.getEntityProperties(this.getEntityInfo(entity.getClass()));

        UpdateWrapper<T> updateWrapper = Wrappers.update();

        this.addSetToUpdateWrapper(updateWrapper, entity, updateProperties);

        this.addWhereToWrapper(entity.getClass(), updateWrapper, where);

        return updateWrapper;
    }

    /**
     * 创建Mybatis plus 分页对象
     *
     * @param pagerRequest
     * @param orderBy
     * @return
     */
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page buildPageFrom(Class tClass, PagerRequest pagerRequest, OrderBy orderBy) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page mybatisPlusPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page();
        mybatisPlusPage.setCurrent(pagerRequest.getPageIndex());
        mybatisPlusPage.setSize(pagerRequest.getPageSize());

        if (Objects.isNull(orderBy) || CollectionUtils.isEmpty(orderBy.getSorts())) {
            return mybatisPlusPage;
        }
        List<SortRequest> sorts = orderBy.getSorts();
        List<OrderItem> orderItems = sorts
                .stream()
                .map(p -> {
                            String columnName = entityMetaDbService.getColumnName(tClass, p.getProperty());
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
        EntityInfo entityInfo = this.getEntityInfo(entity.getClass());
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

    /**
     * 添加Select的字段到QueryWrapper
     *
     * @param queryWrapper
     * @param select
     * @param <T>
     */
    private <T> void addSelectToQueryWrapper(Class<T> tClass, QueryWrapper<T> queryWrapper, Select select) {
        if (Objects.isNull(select) || CollectionUtils.isEmpty(select.getProperties())) {
            return;
        }
        List<String> properties = select.getProperties();
        List<String> columns = properties
                .stream()
                .map(p -> getEntityProperty(tClass, p))
                .filter(p -> p != null)
                .map(p -> p.getColumn())
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(columns)) {
            return;
        }

        queryWrapper.select(columns.toArray(new String[columns.size()]));
    }

    /**
     * 转换 orderBy 条件,转换不为null的字段
     *
     * @param wrapper
     * @param orderBy
     * @param <T>
     */
    private <T> void addOrderByToWrapper(Class<T> tClass, QueryWrapper<T> wrapper, OrderBy orderBy) {
        if (Objects.isNull(orderBy) || CollectionUtils.isEmpty(orderBy.getSorts())) {
            return;
        }
        List<Sort> sorts = orderBy.getSorts();

        sorts.stream().forEachOrdered(sort -> {
            String columnName = getEntityProperty(tClass, sort.getProperty()).getColumn();
            wrapper.orderBy(true, sort.getAsc(), columnName);
        });
    }

    /**
     * 转换where 条件,转换不为null的字段，并自动按entity对象字段类型进行转换，最后添加到UpdateWrapper
     *
     * @param wrapper 实体更新操作实例
     * @param where   Where条件
     * @param <T>     实体类型
     * @return
     */
    private <T, Children extends AbstractWrapper<T, String, Children>>
    void addWhereToWrapper(Class tClass, AbstractWrapper<T, String, Children> wrapper, Where where) {
        if (Objects.isNull(where) || CollectionUtils.isEmpty(where.getConditions())) {
            return;
        }
        List<ace.fw.restful.base.api.model.where.Condition> conditions = where.getConditions();
        conditions
                .stream()
                .forEach(condition -> {
                    addConditionToWrapper(tClass, wrapper, condition);
                });
    }

    /**
     * 添加condition到UpdateWrapper对象
     *
     * @param wrapper   需要添加的UpdateWrapper对象
     * @param condition 准备添加的条件
     * @param <T>       实体类型
     * @return
     */
    private <T, Children extends AbstractWrapper<T, String, Children>>
    void addConditionToWrapper(Class<T> tClass, AbstractWrapper<T, String, Children> wrapper,
                               Condition condition) {
        String columnName = this.entityMetaDbService.getColumnName(tClass, condition.getPropertyName());

        if (LogicalOpEnum.OR.equals(condition.getLogicalOp())) {
            wrapper.or();
        }
        if (RelationalOpEnum.EQ.equals(condition.getRelationalOp())) {
            wrapper.eq(Objects.nonNull(condition.getFirstValue()), columnName, condition.getFirstValue());
        } else if (RelationalOpEnum.NE.equals(condition.getRelationalOp())) {
            wrapper.ne(Objects.nonNull(condition.getFirstValue()), columnName, condition.getFirstValue());
        } else if (RelationalOpEnum.IN.equals(condition.getRelationalOp())) {
            wrapper.in(CollectionUtils.isNotEmpty(condition.getValues()), columnName, condition.getValues());
        } else if (RelationalOpEnum.LIKE.equals(condition.getRelationalOp())) {
            wrapper.like(Objects.nonNull(condition.getFirstValue()), columnName, condition.getFirstValue());
        } else if (RelationalOpEnum.LIKE_LEFT.equals(condition.getRelationalOp())) {
            wrapper.likeLeft(Objects.nonNull(condition.getFirstValue()), columnName, condition.getFirstValue());
        } else if (RelationalOpEnum.LIKE_RIGHT.equals(condition.getRelationalOp())) {
            wrapper.likeRight(Objects.nonNull(condition.getFirstValue()), columnName, condition.getFirstValue());
        } else if (RelationalOpEnum.GT.equals(condition.getRelationalOp())) {
            wrapper.gt(Objects.nonNull(condition.getFirstValue()), columnName, condition.getFirstValue());
        } else if (RelationalOpEnum.GE.equals(condition.getRelationalOp())) {
            wrapper.ge(Objects.nonNull(condition.getFirstValue()), columnName, condition.getFirstValue());
        } else if (RelationalOpEnum.LT.equals(condition.getRelationalOp())) {
            wrapper.lt(Objects.nonNull(condition.getFirstValue()), columnName, condition.getFirstValue());
        } else if (RelationalOpEnum.LE.equals(condition.getRelationalOp())) {
            wrapper.le(Objects.nonNull(condition.getFirstValue()), columnName, condition.getFirstValue());
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
            wrapper.notLike(Objects.nonNull(condition.getFirstValue()), columnName, condition.getFirstValue());
        } else {
            throw new RuntimeException("不支持的RelationalOp操作," + condition.getRelationalOp().getDesc());
        }
    }

    private List<String> getEntityProperties(EntityInfo entityInfo) {
        return entityInfo.getProperties()
                .stream()
                .map(p -> p.getId())
                .collect(Collectors.toList());
    }

    private EntityProperty getEntityProperty(Class cls, String property) {
        return this.getEntityInfo(cls).getProperties()
                .stream()
                .filter(entityProperty -> StringUtils.equals(entityProperty.getId(), property))
                .findFirst()
                .orElse(null);
    }


    private EntityInfo getEntityInfo(Class cls) {
        return entityMetaDbService.getEntityInfo(cls);
    }
}
