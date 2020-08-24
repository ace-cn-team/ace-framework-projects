package ace.fw.mybatis.plus.extension.util;

import ace.fw.data.model.*;
import ace.fw.data.model.entity.Entity;
import ace.fw.data.model.request.resful.WhereRequest;
import ace.fw.exception.SystemException;
import ace.fw.mybatis.plus.extension.model.EntityField;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/7 14:43
 * @description
 */
public class MybatisPlusUtils {
    /**
     * 创建UpdateWrapper
     * 根据指定属性进行强制更新，含null的属性
     *
     * @param updateWrapper    实体更新操作实例
     * @param entity           准备更新的实体实例
     * @param updateProperties 指定更新的属性
     * @param entityInfo       实体元数据
     * @return
     */
    public static <M extends Entity> void addSetToUpdateWrapper(UpdateWrapper<M> updateWrapper, EntityInfo entityInfo, M entity, List<String> updateProperties) {
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
     * 转换where 条件,转换不为null的字段，并自动按entity对象字段类型进行转换，最后添加到UpdateWrapper
     *
     * @param wrapper    实体更新操作实例
     * @param entityInfo 实体元数据
     * @param where      Where条件
     * @param <T>        实体类型
     * @return
     */
    public static <T extends Entity, Children extends AbstractWrapper<T, String, Children>, ConditionT>
    void addWhereToWrapper(AbstractWrapper<T, String, Children> wrapper, EntityInfo entityInfo, WhereRequest where) {
        if (Objects.isNull(where) || CollectionUtils.isEmpty(where.getConditions())) {
            return;
        }
        where.getConditions()
                .stream()
                .forEach(condition -> {
                    addConditionToWrapper(wrapper, entityInfo, condition.getValue() != null, condition);
                });
    }

    /**
     * 添加condition到UpdateWrapper对象
     *
     * @param entityInfo    entity实体相关信息
     * @param wrapper       需要添加的UpdateWrapper对象
     * @param execCondition 添加条件的执行条件
     * @param condition     准备添加的条件
     * @param <T>           实体类型
     * @param <ConditionT>  条件的泛型值
     * @return
     */
    public static <T extends Entity, Children extends AbstractWrapper<T, String, Children>, ConditionT>
    void addConditionToWrapper(AbstractWrapper<T, String, Children> wrapper,
                               EntityInfo entityInfo,
                               boolean execCondition,
                               GenericCondition<ConditionT> condition) {
        String conditionColumn = getColumn(entityInfo, condition.getField());
        if (StringUtils.isEmpty(conditionColumn)) {
            return;
        }
        if (LogicalOpConstVal.OR.equalsIgnoreCase(condition.getLogicalOp())) {
            wrapper.or();
        } else if (RelationalOpConstVal.EQ.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.eq(execCondition, conditionColumn, condition.getValue());
        } else if (RelationalOpConstVal.NE.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.ne(execCondition, conditionColumn, condition.getValue());
        } else if (RelationalOpConstVal.IN.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.in(execCondition, conditionColumn, condition.getValue());
        } else if (RelationalOpConstVal.LIKE.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.like(execCondition, conditionColumn, condition.getValue());
        } else if (RelationalOpConstVal.LIKE_LEFT.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.likeLeft(execCondition, conditionColumn, condition.getValue());
        } else if (RelationalOpConstVal.LIKE_RIGHT.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.likeRight(execCondition, conditionColumn, condition.getValue());
        } else if (RelationalOpConstVal.GT.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.gt(execCondition, conditionColumn, condition.getValue());
        } else if (RelationalOpConstVal.GE.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.ge(execCondition, conditionColumn, condition.getValue());
        } else if (RelationalOpConstVal.LT.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.lt(execCondition, conditionColumn, condition.getValue());
        } else if (RelationalOpConstVal.LE.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.le(execCondition, conditionColumn, condition.getValue());
        } else if (RelationalOpConstVal.IS_NULL.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.isNull(execCondition, conditionColumn);
        } else if (RelationalOpConstVal.IS_NOT_NULL.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.isNotNull(execCondition, conditionColumn);
        } else if (RelationalOpConstVal.BETWEEN.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.between(execCondition, conditionColumn, condition.getValue(), condition.getOtherValue());
        } else if (RelationalOpConstVal.NOT_BETWEEN.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.notBetween(execCondition, conditionColumn, condition.getValue(), condition.getOtherValue());
        } else if (RelationalOpConstVal.NOT_IN.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.notIn(execCondition, conditionColumn, condition.getValue());
        } else if (RelationalOpConstVal.NOT_LIKE.equalsIgnoreCase(condition.getRelationalOp())) {
            wrapper.notLike(execCondition, conditionColumn, condition.getValue());
        }
    }

    /**
     * 获取属性对应的column名称
     *
     * @param entityInfo   实体相关信息
     * @param propertyName 实体的属性名称
     * @return 列名或者null
     */
    public static String getColumn(EntityInfo entityInfo, String propertyName) {
        String column = entityInfo
                .getProperties()
                .stream()
                .filter(p -> StringUtils.equalsIgnoreCase(p.getId(), propertyName))
                .findFirst()
                .map(p -> p.getColumn())
                .orElse(StringUtils.EMPTY);

        return column;
    }

    /**
     * 添加Select的字段到QueryWrapper
     *
     * @param queryWrapper
     * @param entityInfo
     * @param select
     * @param <T>
     */
    public static <T> void addSelectToQueryWrapper(QueryWrapper<T> queryWrapper, EntityInfo entityInfo, Select select) {
        if (Objects.isNull(select) || CollectionUtils.isEmpty(select.getFields())) {
            return;
        }

        List<String> columns = select
                .getFields()
                .stream()
                .map(p -> getEntityProperty(entityInfo, p))
                .filter(p -> p != null)
                .map(p -> p.getColumn())
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(columns)) {
            return;
        }

        queryWrapper.select(columns.toArray(new String[columns.size()]));
    }

    /**
     * 创建Mybatis plus 分页对象
     *
     * @param entityInfo
     * @param pageRequest
     * @param orderBy
     * @return
     */
    public static <T extends Entity> Page<T> buildPageFrom(EntityInfo entityInfo, PageRequest pageRequest, OrderBy orderBy) {
        Page pageDbRequest = new Page()
                .setCurrent(pageRequest.getPageIndex())
                .setSize(pageRequest.getPageSize());

        if (Objects.isNull(orderBy) || CollectionUtils.isEmpty(orderBy.getSorts())) {
            return pageDbRequest;
        }

        List<OrderItem> orderItems = orderBy.getSorts()
                .stream()
                .map(p -> new OrderItem()
                        .setAsc(p.getAsc())
                        .setColumn(getColumn(entityInfo, p.getField()))
                )
                .collect(Collectors.toList());

        pageDbRequest.addOrder(orderItems);

        return pageDbRequest;
    }

    /**
     * 创建restful 分页对象
     *
     * @param pageResult
     * @return
     */
    public static <T extends Entity> PageResponse<T> buildPageResponseFrom(IPage<T> pageResult) {
        PageResponse<T> pageResponse = new PageResponse<T>()
                .setTotalCount(pageResult.getTotal())
                .setTotalPage(pageResult.getPages())
                .setData(pageResult.getRecords());
        return pageResponse;
    }

    public static EntityProperty getEntityProperty(EntityInfo entityInfo, String property) {
        return entityInfo.getProperties()
                .stream()
                .filter(entityProperty -> StringUtils.equals(entityProperty.getId(), property))
                .findFirst()
                .orElse(null);
    }

    private static final Map<Class<?>, EntityField> versionFieldCache = new ConcurrentHashMap<>();
    private static final Map<Class<?>, List<EntityField>> entityFieldsCache = new ConcurrentHashMap<>();

    /**
     * 获取版本号字段信息
     *
     * @param parameterClass
     * @param tableInfo
     * @return
     */
    public static EntityField getVersionField(Class<?> parameterClass, TableInfo tableInfo) {
        return versionFieldCache.computeIfAbsent(parameterClass, mapping -> getVersionFieldRegular(parameterClass, tableInfo));
    }

    /**
     * 反射检查参数类是否启动乐观锁
     *
     * @param parameterClass 实体类
     * @param tableInfo      实体数据库反射信息
     * @return ignore
     */
    private static EntityField getVersionFieldRegular(Class<?> parameterClass, TableInfo tableInfo) {
        return Object.class.equals(parameterClass) ? null : ReflectionKit.getFieldList(parameterClass).stream().filter(e -> e.isAnnotationPresent(Version.class)).map(field -> {
            field.setAccessible(true);
            return new EntityField(field, true, tableInfo.getFieldList().stream().filter(e -> field.getName().equals(e.getProperty())).map(TableFieldInfo::getColumn).findFirst().orElse(null));
        }).findFirst().orElseGet(() -> getVersionFieldRegular(parameterClass.getSuperclass(), tableInfo));
    }
}
