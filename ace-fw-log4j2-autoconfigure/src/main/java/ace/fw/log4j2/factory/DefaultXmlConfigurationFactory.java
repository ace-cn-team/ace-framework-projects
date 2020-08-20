//package ace.fw.log4j2.factory;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.logging.log4j.core.LoggerContext;
//import org.apache.logging.log4j.core.config.Configuration;
//import org.apache.logging.log4j.core.config.ConfigurationFactory;
//import org.apache.logging.log4j.core.config.ConfigurationSource;
//import org.apache.logging.log4j.core.config.Order;
//import org.apache.logging.log4j.core.config.plugins.Plugin;
//import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;
//import org.apache.logging.log4j.spi.LoggerContextFactory;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.Objects;
//
///**
// * @author Caspar
// * @contract 279397942@qq.com
// * @create 2020/3/8 10:01
// * @description
// */
//@Plugin(name = "DefaultXmlConfigurationFactory", category = ConfigurationFactory.CATEGORY)
//@Order(10)
//@Slf4j
//public class DefaultXmlConfigurationFactory extends XmlConfigurationFactory {
//
//
//    @Override
//    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
//        URI newconfigLocation = null;
//        if (Objects.isNull(configLocation)) {
//            try {
//                newconfigLocation = new URI("classpath:log4j2-spring.xml");
//            } catch (Exception e) {
//                log.error("读取[/log4j2-spring.xml]失败", e);
//                newconfigLocation = configLocation;
//            }
//        } else {
//            newconfigLocation = configLocation;
//        }
//
//        Configuration configuration = super.getConfiguration(loggerContext, name, newconfigLocation);
//
//        return configuration;
//
//    }
//
//    @Override
//    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation, final ClassLoader loader) {
//        return super.getConfiguration(loggerContext, name, configLocation, loader);
//    }
//}