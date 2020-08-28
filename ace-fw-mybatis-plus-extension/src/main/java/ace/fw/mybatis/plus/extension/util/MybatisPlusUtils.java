package ace.fw.mybatis.plus.extension.util;

import ace.fw.mybatis.plus.extension.model.EntityField;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/7 14:43
 * @description
 */
public class MybatisPlusUtils {


    private static final Map<Class<?>, EntityField> versionFieldCache = new ConcurrentHashMap<>();

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
