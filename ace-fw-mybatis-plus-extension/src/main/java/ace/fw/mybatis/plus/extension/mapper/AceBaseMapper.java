package ace.fw.mybatis.plus.extension.mapper;

import ace.fw.mybatis.plus.extension.mapper.impl.AceBaseMapperImpl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/24 10:37
 * @description
 */
public interface AceBaseMapper<T> extends BaseMapper<T> {

    /**
     * 根据ID，更新对象,null字段不进行更新,version字段自动更新(输入指定值，自动进行替换)
     * version字段类型支持{@link Integer}{@link Long}{@link java.util.Date}{@link java.time.LocalDateTime}{@link java.sql.Timestamp}
     *
     * @param entity
     * @return
     */
    @UpdateProvider(type = AceBaseMapperImpl.class, method = "updateByIdVersionAutoUpdate")
    Integer updateByIdVersionAutoUpdate(@Param("et") T entity);

    /**
     * 根据ID，批量更新对象,null字段不进行更新,version字段自动更新(输入指定值，自动进行替换)
     * version字段类型支持{@link Integer}{@link Long}{@link java.util.Date}{@link java.time.LocalDateTime}{@link java.sql.Timestamp}
     *
     * @param entity
     * @return
     */
    @UpdateProvider(type = AceBaseMapperImpl.class, method = "updateBatchByIdVersionAutoUpdate")
    Integer updateBatchByIdVersionAutoUpdate(@Param("et") List<T> entities);
}
