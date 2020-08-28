package ace.fw.mybatis.plus.extension.mapper;

import ace.fw.mybatis.plus.extension.mapper.impl.AceBaseMapperImpl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/24 10:37
 * @description
 */
public interface AceBaseMapper<T> extends BaseMapper<T> {

    /**
     * 根据ID，更新对象,null字段不进行更新,version字段自动更新(输入指定值，自动进行替换)
     * version字段类型支持{@link Integer}{@link Long}
     * version字段类型{@link Integer}指定值{@link Integer#MIN_VALUE)
     * version字段类型{@link Long}指定值{@link Long#MIN_VALUE)
     *
     * @param entity
     * @return
     */
    @UpdateProvider(type = AceBaseMapperImpl.class, method = "updateByIdVersionAutoUpdate")
    Integer updateByIdVersionAutoUpdate(@Param("et") T entity);
}
