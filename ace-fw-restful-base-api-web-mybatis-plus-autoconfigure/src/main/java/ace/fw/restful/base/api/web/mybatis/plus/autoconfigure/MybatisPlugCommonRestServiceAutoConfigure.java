package ace.fw.restful.base.api.web.mybatis.plus.autoconfigure;

import ace.fw.data.model.entity.Entity;
import ace.fw.data.service.UniformDbService;
import ace.fw.mybatis.plus.extension.mapper.BaseMapper;
import ace.fw.mybatis.plus.extension.model.EntityServiceInfo;
import ace.fw.mybatis.plus.extension.service.EntityConfigInfoService;
import ace.fw.mybatis.plus.extension.service.EntityServiceInfoService;
import ace.fw.mybatis.plus.extension.service.MybatisPlusDbService;
import ace.fw.mybatis.plus.extension.service.impl.EntityConfigInfoServiceImpl;
import ace.fw.mybatis.plus.extension.service.impl.EntityServiceInfoServiceImpl;
import ace.fw.mybatis.plus.extension.service.impl.MybatisPlusUniformBizServiceImpl;
import ace.fw.restful.base.api.service.CommonRestfulBaseService;
import ace.fw.restful.base.api.web.CommonRestfulController;
import ace.fw.restful.base.api.web.mybatis.plus.autoconfigure.resolver.EntityConfigResolver;
import ace.fw.restful.base.api.web.mybatis.plus.autoconfigure.resolver.impl.DefaultEntityConfigResolver;
import ace.fw.restful.base.api.web.mybatis.plus.autoconfigure.resolver.impl.DefaultIServiceConfigResolver;
import ace.fw.restful.base.api.web.mybatis.plus.autoconfigure.resolver.IServiceConfigResolver;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusLanguageDriverAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/9 11:41
 * @description
 */
@Configuration
@AutoConfigureAfter({
        MybatisPlusLanguageDriverAutoConfiguration.class,
        MybatisPlusAutoConfiguration.class,
})
public class MybatisPlugCommonRestServiceAutoConfigure {

    @Bean
    public IServiceConfigResolver iServiceConfigResolver(@Autowired List<MybatisPlusDbService> mybatisPlusDbServices) {

        IServiceConfigResolver resolver = new DefaultIServiceConfigResolver().setMybatisPlusDbServices(mybatisPlusDbServices);
        return resolver;
    }

    @Bean
    public EntityServiceInfoService entityServiceInfoService(IServiceConfigResolver iServiceConfigResolver) {
        Map<String, EntityServiceInfo<Entity>> entityServiceInfoMap = iServiceConfigResolver.resolve();
        EntityServiceInfoService entityServiceInfoService = new EntityServiceInfoServiceImpl()
                .setEntityServiceInfoMap(entityServiceInfoMap);
        return entityServiceInfoService;
    }


    @Bean
    public UniformDbService commonDbService(EntityServiceInfoService entityServiceInfoService) {
        UniformDbService baseService = new MybatisPlusUniformBizServiceImpl()
                .setEntityServiceInfoService(entityServiceInfoService);
        return baseService;
    }

    @Bean
    public CommonRestfulBaseService<Entity> commonRestController(UniformDbService<Entity> baseService) {
        CommonRestfulController commonRestController = new CommonRestfulController()
                .setUniformDbService(baseService);
        return commonRestController;
    }

    @Bean
    public EntityConfigResolver entityConfigResolver(@Autowired List<BaseMapper> baseMapperList) {
        EntityConfigResolver entityConfigResolver = new DefaultEntityConfigResolver()
                .setBaseMapperList(baseMapperList);
        return entityConfigResolver;
    }

    @Bean
    public EntityConfigInfoService entityConfigInfoService(EntityConfigResolver entityConfigResolver) {
        EntityConfigInfoService entityConfigInfoService = new EntityConfigInfoServiceImpl()
                .setEntityServiceInfoMap(entityConfigResolver.resolve());
        return entityConfigInfoService;
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        //paginationInterceptor.setLimit(500);
        return paginationInterceptor;
    }

    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

}
