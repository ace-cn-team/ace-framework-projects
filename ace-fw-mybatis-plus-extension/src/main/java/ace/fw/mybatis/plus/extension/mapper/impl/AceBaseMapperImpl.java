package ace.fw.mybatis.plus.extension.mapper.impl;

import ace.fw.mybatis.plus.extension.mapper.AceBaseMapper;
import ace.fw.mybatis.plus.extension.model.EntityField;
import ace.fw.mybatis.plus.extension.util.MybatisPlusUtils;
import ace.fw.util.AceStringUtils;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    /**
     * 根据ID，更新对象,null字段不进行更新,version字段自动更新(输入指定值，自动进行替换)
     * version字段类型支持{@link Integer}{@link Long}
     * version字段类型{@link Integer}指定值{@link Integer#MIN_VALUE)
     * version字段类型{@link Long}指定值{@link Long#MIN_VALUE)
     *
     * @param entity
     * @return
     */
    public String updateByIdVersionAutoUpdate(Map<String, Object> params) {
        Object entity = params.get(PARAM_ENTITY_KEY);
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
        String tableName = tableInfo.getTableName();
        List<String> sets = this.getSets(tableInfo, entity);
        List<String> where = this.getWhere(tableInfo);
        SQL sql = new SQL();
        sql.UPDATE(tableName);
        sql.SET(sets.toArray(new String[sets.size()]));
        sql.WHERE(where.toArray(new String[where.size()]));
        String result = sql.toString();
        if (log.isDebugEnabled()) {
            log.debug(result);
        }
        return result;
    }

    private List<String> getWhere(TableInfo tableInfo) {
        String keyColumnName = tableInfo.getKeyColumn();
        String keyPropertyName = tableInfo.getKeyProperty();
        return Arrays.asList(
                AceStringUtils.replaceEach(
                        SQL_WHERE_TEMPLATE,
                        Pair.of(SQL_KEY_COLUMN_NAME, keyColumnName),
                        Pair.of(SQL_KEY_PROPERTY_VALUE, String.format("#{%s}", getTableFieldPropertyMybatisString(keyPropertyName)))
                )
        );
    }

    private List<String> getSets(TableInfo tableInfo, Object entity) {
        List<String> sets = new ArrayList<>(tableInfo.getFieldList().size());
        for (TableFieldInfo tableFieldInfo : tableInfo.getFieldList()) {
            // 获取字段的值
            Object value = getFieldValueFromEntity(entity, tableFieldInfo);
            if (value == null) {
                continue;
            }

            // 获取列的赋值,版本号的列自动加1，其它列的值不改变
            String propertyValueString = this.getVersionPropertyOrPropertyValueString(entity, tableInfo, tableFieldInfo);

            String set = AceStringUtils.replaceEach(
                    SQL_SET_TEMPLATE,
                    Pair.of(SQL_KEY_COLUMN_NAME, tableFieldInfo.getColumn()),
                    Pair.of(SQL_KEY_PROPERTY_NAME, getTableFieldPropertyMybatisString(tableFieldInfo.getProperty())),
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
     * 获取列的赋值,版本列自动加1，其它不改变
     *
     * @return
     */
    private String getVersionPropertyOrPropertyValueString(Object entity, TableInfo tableInfo, TableFieldInfo selectTableFieldInfo) {

        Boolean isVersionFieldAutoUpdate = isVersionAutoUpdate(entity, tableInfo, selectTableFieldInfo);
        if (isVersionFieldAutoUpdate) {
            return AceStringUtils.replaceEach(
                    "${columnName}+1",
                    Pair.of(SQL_KEY_COLUMN_NAME, selectTableFieldInfo.getColumn()
                    )
            );
        } else {
            return AceStringUtils.replaceEach(
                    "#{${propertyName}}",
                    Pair.of(SQL_KEY_PROPERTY_NAME, getTableFieldPropertyMybatisString(selectTableFieldInfo.getProperty()))
            );
        }
    }

    @SneakyThrows
    private boolean isVersionAutoUpdate(Object entity, TableInfo tableInfo, TableFieldInfo selectTableFieldInfo) {
        EntityField versionField = MybatisPlusUtils.getVersionField(entity.getClass(), tableInfo);
        if (versionField == null) {
            return false;
        }
        if (versionField.getColumnName().equals(selectTableFieldInfo.getColumn()) == false) {
            return false;
        }
        Object value = versionField.getField().get(entity);
        if (value == null) {
            return false;
        }
        return (value instanceof Integer) ||
                (value instanceof Long);

    }

    private String getTableFieldPropertyMybatisString(String propertyName) {
        return PARAM_ENTITY_KEY + "." + propertyName;
    }
}
