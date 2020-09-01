package ace.fw.mybatis.plus.extension.mapper.impl;

import ace.fw.mybatis.plus.extension.model.EntityField;
import ace.fw.mybatis.plus.extension.util.MybatisPlusUtils;
import ace.fw.util.AceStringUtils;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.jdbc.SQL;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/24 10:42
 * @description
 */
@Slf4j
public class AceBaseMapperImpl {
    private final static String SQL_SET_TEMPLATE = String.format("%s=%s",
            AceBaseMapperImpl.SQL_KEY_COLUMN_NAME,
            AceBaseMapperImpl.SQL_KEY_PROPERTY_VALUE
    );
    private final static String SQL_WHERE_TEMPLATE = String.format("%s=%s",
            AceBaseMapperImpl.SQL_KEY_COLUMN_NAME,
            AceBaseMapperImpl.SQL_KEY_PROPERTY_VALUE
    );
    private final static String SQL_KEY_COLUMN_NAME = "${columnName}";
    private final static String SQL_KEY_PROPERTY_NAME = "${propertyName}";
    private final static String SQL_KEY_PROPERTY_VALUE = "${propertyValue}";
    private final static String PARAM_ENTITY_KEY = "param1";
    private final static Map<Class, Boolean> VERSION_SUPPORT_NUMERIC_TYPE = new HashMap<>() {
        {
            put(Integer.class, true);
            put(Long.class, true);
        }
    };
    /**
     * 支持自动更新的字段类型
     */
    private final static Map<Class, Boolean> VERSION_SUPPORT_TYPE = new HashMap<>() {
        {
            putAll(VERSION_SUPPORT_NUMERIC_TYPE);
            put(Date.class, true);
            put(Timestamp.class, true);
            put(LocalDateTime.class, true);
        }
    };

    /**
     * 根据ID，更新对象,null字段不进行更新,version字段自动更新(输入指定值，自动进行替换)
     * version字段类型支持{@link Integer}{@link Long}{@link java.util.Date}{@link java.time.LocalDateTime}{@link java.sql.Timestamp}
     *
     * @param entity
     * @return
     */
    public String updateByIdVersionAutoUpdate(Map<String, Object> params) {
        Object entity = params.get(PARAM_ENTITY_KEY);
        String result = getSingleUpdateByIdVersionAutoUpdateSql(entity, -1);
        if (log.isDebugEnabled()) {
            log.debug(result);
        }
        return result;
    }

    public String updateBatchByIdVersionAutoUpdate(Map<String, Object> params) {
        List entities = (List) params.get(PARAM_ENTITY_KEY);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < entities.size(); i++) {
            Object entity = entities.get(i);
            sb.append(this.getSingleUpdateByIdVersionAutoUpdateSql(entity, i));
            sb.append(";");
            sb.append(System.lineSeparator());
        }
        String result = sb.toString();
        if (log.isDebugEnabled()) {
            log.debug(result);
        }
        return result;
    }

    /**
     * 构建 update 语句,index少于0，代表生成单一对象操作，大于0，代表生成批量操作
     *
     * @param entity
     * @param index
     * @return
     */
    private String getSingleUpdateByIdVersionAutoUpdateSql(Object entity, int index) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
        String tableName = tableInfo.getTableName();
        List<String> sets = this.getSets(tableInfo, entity, index);
        List<String> where = this.getWhere(tableInfo, index);
        SQL sql = new SQL();
        sql.UPDATE(tableName);
        sql.SET(sets.toArray(new String[sets.size()]));
        sql.WHERE(where.toArray(new String[where.size()]));
        String result = sql.toString();
        return result;
    }

    private List<String> getWhere(TableInfo tableInfo, int index) {
        String keyColumnName = tableInfo.getKeyColumn();
        String keyPropertyName = tableInfo.getKeyProperty();
        return Arrays.asList(
                AceStringUtils.replaceEach(
                        SQL_WHERE_TEMPLATE,
                        Pair.of(SQL_KEY_COLUMN_NAME, keyColumnName),
                        Pair.of(SQL_KEY_PROPERTY_VALUE, String.format("#{%s}", getTableFieldPropertyMybatisString(keyPropertyName, index)))
                )
        );
    }

    private List<String> getSets(TableInfo tableInfo, Object entity, int index) {
        List<String> sets = new ArrayList<>(tableInfo.getFieldList().size());
        for (TableFieldInfo tableFieldInfo : tableInfo.getFieldList()) {
            String propertyValueString = this.getVersionPropertyOrPropertyValueString(entity, tableInfo, tableFieldInfo, index);

            if (StringUtils.isBlank(propertyValueString)) {
                continue;
            }

            String set = AceStringUtils.replaceEach(
                    SQL_SET_TEMPLATE,
                    Pair.of(SQL_KEY_COLUMN_NAME, tableFieldInfo.getColumn()),
                    Pair.of(SQL_KEY_PROPERTY_NAME, getTableFieldPropertyMybatisString(tableFieldInfo.getProperty(), index)),
                    Pair.of(SQL_KEY_PROPERTY_VALUE, propertyValueString)
            );
            sets.add(set);
        }
        return sets;
    }

    @SneakyThrows
    private Object getFieldValueFromEntity(Object entity, TableFieldInfo tableFieldInfo) {
        return FieldUtils.getField(entity.getClass(), tableFieldInfo.getProperty(), true).get(entity);
    }

    /**
     * 获取列的赋值
     *
     * @return 1.版本号列，sql自动加1语句
     * 2.非版本号列的值直接赋值
     * 3.如果赋值的值为null，返回空字符串
     */
    private String getVersionPropertyOrPropertyValueString(Object entity, TableInfo tableInfo, TableFieldInfo selectTableFieldInfo, int index) {

        if (this.isVersionAutoUpdate(entity, tableInfo, selectTableFieldInfo)) {
            // 获取自动赋值的列的赋值
            return this.getVersionFieldPropertyValue(selectTableFieldInfo);
        }
        // 非版本号字段
        // 获取字段的值
        Object value = getFieldValueFromEntity(entity, selectTableFieldInfo);
        // 值为null，返回空字符串
        if (value == null) {
            return StringUtils.EMPTY;
        }
        // 获取列的赋值
        return AceStringUtils.replaceEach(
                "#{${propertyName}}",
                Pair.of(SQL_KEY_PROPERTY_NAME, getTableFieldPropertyMybatisString(selectTableFieldInfo.getProperty(), index))
        );
    }

    private String getVersionFieldPropertyValue(TableFieldInfo versionFieldInfo) {
        if (isNumericVersionType(versionFieldInfo)) {
            return AceStringUtils.replaceEach(
                    "${columnName}+1",
                    Pair.of(SQL_KEY_COLUMN_NAME, versionFieldInfo.getColumn()
                    )
            );
        } else {
            return "NOW()";
        }
    }

    @SneakyThrows
    private boolean isVersionAutoUpdate(Object entity, TableInfo tableInfo, TableFieldInfo selectTableFieldInfo) {
        EntityField versionField = this.getVersionFields(entity, tableInfo, selectTableFieldInfo);
        if (versionField == null) {
            return false;
        }
        if (versionField.getColumnName().equals(selectTableFieldInfo.getColumn()) == false) {
            return false;
        }
        return VERSION_SUPPORT_TYPE.containsKey(versionField.getField().getType());
    }

    private EntityField getVersionFields(Object entity, TableInfo tableInfo, TableFieldInfo selectTableFieldInfo) {
        List<EntityField> entityFields = MybatisPlusUtils.getVersionFields(entity.getClass(), tableInfo);
        if (CollectionUtils.isEmpty(entityFields)) {
            return null;
        }
        return entityFields.stream()
                .filter(p -> StringUtils.equals(p.getColumnName(), selectTableFieldInfo.getColumn()))
                .findFirst()
                .orElse(null);
    }

    private boolean isNumericVersionType(TableFieldInfo versionFieldInfo) {
        return VERSION_SUPPORT_NUMERIC_TYPE.containsKey(versionFieldInfo.getPropertyType());
    }

    private String getTableFieldPropertyMybatisString(String propertyName, int index) {
        if (index < 0) {
            return PARAM_ENTITY_KEY + "." + propertyName;
        } else {
            return String.format("%s[%s].%s", PARAM_ENTITY_KEY, index, propertyName);
        }
    }
}
