package ace.fw.restful.base.api.plugin;

import ace.fw.restful.base.api.model.EntityInfo;
import ace.fw.restful.base.api.model.EntityPropertyFunction;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/27 13:39
 * @description 数据访问层-实体映射元数据插件
 */
public interface EntityMetaDbService {
    /**
     * 获取服务相关业务对象元数据
     *
     * @return
     */
    EntityInfo getEntityInfo(Class cls);

    /**
     * 获取实体字段映射数据库字段的实体字段名称
     *
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    <T, R> String getPropertyName(EntityPropertyFunction<T, R> function);

    /**
     * 获取实体映射数据库字段的列名称
     *
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    <T, R> String getColumnName(EntityPropertyFunction<T, R> function);

    /**
     * 获取实体属性映射对应的列名
     *
     * @param cls          实体类类型
     * @param propertyName 实体属性名
     * @return 列名
     */
    String getColumnName(Class cls, String propertyName);
}
