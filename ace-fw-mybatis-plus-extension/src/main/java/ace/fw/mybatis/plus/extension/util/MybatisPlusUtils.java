package ace.fw.mybatis.plus.extension.util;

import ace.fw.mybatis.plus.extension.model.EntityField;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/7 14:43
 * @description
 */
public class MybatisPlusUtils {


    private static final Map<Class<?>, List<EntityField>> versionFieldCache = new ConcurrentHashMap<>();

    /**
     * 获取版本号字段信息
     *
     * @param parameterClass
     * @param tableInfo
     * @return
     */
    public static List<EntityField> getVersionFields(Class<?> parameterClass, TableInfo tableInfo) {
        return versionFieldCache.computeIfAbsent(parameterClass, mapping -> getVersionFieldRegular(parameterClass, tableInfo));
    }

    /**
     * 反射检查参数类是否启动乐观锁
     *
     * @param parameterClass 实体类
     * @param tableInfo      实体数据库反射信息
     * @return ignore
     */
    private static List<EntityField> getVersionFieldRegular(Class<?> parameterClass, TableInfo tableInfo) {
        if (Object.class.equals(parameterClass)) {
            return null;
        }

        List<EntityField> entityFields = ReflectionKit.getFieldList(parameterClass).stream()
                .filter(e -> e.isAnnotationPresent(Version.class))
                .map(field -> {
                    field.setAccessible(true);
                    return new EntityField(field, true, tableInfo.getFieldList().stream().filter(e -> field.getName().equals(e.getProperty())).map(TableFieldInfo::getColumn).findFirst().orElse(null));
                }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(entityFields)) {
            return entityFields;
        }
        return getVersionFieldRegular(parameterClass.getSuperclass(), tableInfo);
    }
}
