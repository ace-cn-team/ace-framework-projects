package ace.fw.ms.application.constant;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/10 18:38
 * @description
 */
public final class AceWebApplicationBootstrapConstant {
    /**
     * 启动程序扫描配置的包路径
     */
    public final static String COMPONENT_SCAN_APPLICATION_CONFIG_PACKAGE = "ace.**.application.config";
    /**
     * 启动程序扫描controller接口的包路径
     */
    public final static String CONTROLLER_SCAN_APPLICATION_CONFIG_PACKAGE = "ace.**.application.controller";
    /**
     * 默认异常处理路径
     */
    public final static String MVC_DEFAULT_ERROR_PATH = "/error";
}
