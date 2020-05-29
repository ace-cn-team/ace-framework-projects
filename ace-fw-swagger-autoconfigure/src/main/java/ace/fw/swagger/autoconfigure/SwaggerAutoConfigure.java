//package ace.fw.swagger.autoconfigure;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
///**
// * @author Caspar
// * @contract 279397942@qq.com
// * @create 2020/5/26 18:38
// * @description swagger组件自动配置
// */
//@Configuration
//@ConditionalOnProperty(name = "swagger2.enabled", havingValue = "true")
//
//public class SwaggerAutoConfigure {
//
//        @Value("${swagger2.title:swagger2.title没设置}")
//        private String title;
//        @Value("${swagger2.desc:swagger2.desc没设置}")
//        private String desc;
//        @Value("${swagger2.version:swagger2.version没设置}")
//        private String version;
//        @Value("${swagger2.url:swagger2.url没设置}")
//        private String url;
//
//        @Bean
//        public Docket createRestApi() {
//            return new Docket(DocumentationType.SWAGGER_2)
//                    .enable()
////                .enable(enableRestApi)
//                    .apiInfo(apiInfo())
//                    .useDefaultResponseMessages(false)
//                    .select()
//                    .apis(RequestHandlerSelectors.basePackage(Application.class.getPackage().getName()))
//                    .paths(PathSelectors.any())
//                    .build();
//        }
//
//        private ApiInfo apiInfo() {
//            return new ApiInfoBuilder()
//                    .title(title)
//                    .description(desc)
//                    .termsOfServiceUrl(url)
//                    .contact(new Contact("TAPD", "https://www.tapd.cn/62061237/prong/iterations/card_view", null))
//                    .version(version)
//                    .build();
//        }
//    }