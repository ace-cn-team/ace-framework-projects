package ace.fw.restful.base.api.web.mybatis.plus.autoconfigure;


import ace.fw.restful.base.api.plugin.EntityMetaDbService;
import ace.fw.restful.base.api.plugin.mybatisplus.impl.EntityMetaDbServiceImpl;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusLanguageDriverAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
public class MybatisPlugRestfulAutoConfigure {

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
    @ConditionalOnProperty(value = "ace.mybatis.plug.optimistic-locker-interceptor.enable", havingValue = "true", matchIfMissing = true)
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public EntityMetaDbService entityMetaPlugin() {
        return new EntityMetaDbServiceImpl();
    }
}
