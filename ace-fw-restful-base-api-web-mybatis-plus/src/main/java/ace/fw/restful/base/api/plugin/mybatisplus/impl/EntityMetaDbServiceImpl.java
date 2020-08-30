package ace.fw.restful.base.api.plugin.mybatisplus.impl;

import ace.fw.exception.SystemException;
import ace.fw.restful.base.api.model.entity.EntityInfo;
import ace.fw.restful.base.api.model.entity.EntityProperty;
import ace.fw.restful.base.api.model.entity.EntityPropertyFunction;
import ace.fw.restful.base.api.plugin.EntityMetaDbService;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.annotation.Annotation;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/27 13:39
 * @description 数据访问层-实体映射元数据插件
 */
@Slf4j
public class EntityMetaDbServiceImpl implements EntityMetaDbService {
    private static Map<Class, EntityInfo> ENTITY_INFO_CACHE = new ConcurrentHashMap<>(10);
    private static Map<Class, SerializedLambda> CLASS_LAMDBA_CACHE = new ConcurrentHashMap<>();
    private static final String METHOD_WRITE_REPLACE = "writeReplace";

    /**
     * 获取服务相关业务对象元数据
     *
     * @return
     */
    public EntityInfo getEntityInfo(Class cls) {
        EntityInfo entityInfo = ENTITY_INFO_CACHE.get(cls);

        if (entityInfo != null) {
            return entityInfo;
        }

        List<EntityProperty> properties = this.resolveEntityProperties(cls);

        entityInfo = EntityInfo.builder()
                .id(cls.getName())
                .remark(this.resolveEntityRemark(cls))
                .properties(properties)
                .build();

        ENTITY_INFO_CACHE.put(cls, entityInfo);

        return entityInfo;
    }

    /**
     * 获取实体字段映射数据库字段的实体字段名称
     *
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    @Override
    public <T, R> String getPropertyName(EntityPropertyFunction<T, R> function) {
        SerializedLambda serializedLambda = this.getSerializedLambda(function);
        String propertyName = PropertyNamer.methodToProperty(serializedLambda.getImplMethodName());
        return propertyName;
    }

    /**
     * 获取实体映射数据库字段的列名称
     *
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    @Override
    public <T, R> String getColumnName(EntityPropertyFunction<T, R> function) {
        SerializedLambda serializedLambda = this.getSerializedLambda(function);
        String propertyName = this.getPropertyName(function);
        TableInfo tableInfo = null;
        try {
            Class cls = Class.forName(serializedLambda.getImplClass().replaceAll("/", "."));
            tableInfo = TableInfoHelper.getTableInfo(cls);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (StringUtils.equals(tableInfo.getKeyProperty(), propertyName)) {
            return tableInfo.getKeyColumn();
        }
        return tableInfo.getFieldList().stream().filter(p -> StringUtils.equals(p.getProperty(), propertyName)).findFirst().get().getColumn();
    }

    @Override
    public String getColumnName(Class cls, String propertyName) {
        Optional<EntityProperty> entityPropertyOptional = this.getEntityInfo(cls).getProperties().stream()
                .filter(p -> StringUtils.equals(p.getId(), propertyName))
                .findFirst();
        if (entityPropertyOptional.isEmpty()) {
            return StringUtils.EMPTY;
        }
        return entityPropertyOptional.get().getColumn();
    }


    /**
     * 关键在于这个方法
     */
    public static <T, R> SerializedLambda getSerializedLambda(EntityPropertyFunction<T, R> fn) {
        SerializedLambda lambda = CLASS_LAMDBA_CACHE.get(fn.getClass());
        if (lambda == null) {
            try {
                Method method = fn.getClass().getDeclaredMethod(METHOD_WRITE_REPLACE);
                method.setAccessible(Boolean.TRUE);
                lambda = (SerializedLambda) method.invoke(fn);
                CLASS_LAMDBA_CACHE.put(fn.getClass(), lambda);
            } catch (Exception ex) {
                log.error("无法解析lambda", ex);
            }
        }
        return lambda;
    }

    /**
     * 解析实体备注信息
     *
     * @param entityClass
     * @return
     */
    private String resolveEntityRemark(Class entityClass) {
        Annotation annotation = entityClass.getDeclaredAnnotation(ApiModel.class);
        if (annotation == null) {
            return "";
        }
        ApiModel apiModelAnnotation = (ApiModel) annotation;
        String entityRemark = apiModelAnnotation.description();
        return entityRemark;
    }

    /**
     * 解析实体属性信息
     *
     * @param entityClass
     * @return
     */
    private List<EntityProperty> resolveEntityProperties(Class entityClass) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        return Arrays.asList(entityClass.getDeclaredFields())
                .stream()
                .filter(p -> Modifier.isPrivate(p.getModifiers()) || Modifier.isProtected(p.getModifiers()))
                .map(field -> {
                    String propertyId = field.getName();
                    String propertyRemark = this.resolveEntityPropertyRemark(field);
                    Class fieldClass = field.getType();
                    String column = StringUtils.EMPTY;
                    if (StringUtils.equalsIgnoreCase(tableInfo.getKeyProperty(), propertyId)) {
                        column = tableInfo.getKeyColumn();
                    }
                    if (StringUtils.isEmpty(column)) {
                        TableFieldInfo tableFieldInfo = tableInfo.getFieldList()
                                .stream()
                                .filter(p -> StringUtils.equalsIgnoreCase(p.getProperty(), propertyId))
                                .findFirst()
                                .orElse(null);
                        if (Objects.nonNull(tableFieldInfo)) {
                            column = tableFieldInfo.getColumn();
                        }
                    }
                    if (StringUtils.isEmpty(column)) {
                        throw new SystemException(String.format("%s 无法查询对应的column", propertyId));
                    }
                    return EntityProperty.builder()
                            .id(propertyId)
                            .remark(propertyRemark)
                            .type(fieldClass)
                            .column(column)
                            .build();
                })
                .collect(Collectors.toList());

    }

    /**
     * 解析实体属性备注信息
     *
     * @param field
     * @return
     */
    private String resolveEntityPropertyRemark(Field field) {
        ApiModelProperty apiModelPropertyAnnotation = field.getDeclaredAnnotation(ApiModelProperty.class);
        if (apiModelPropertyAnnotation == null) {
            return "";
        }
        return apiModelPropertyAnnotation.value();
    }
}
