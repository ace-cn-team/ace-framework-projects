package ace.fw.restful.base.api.model.orderby;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/30 22:14
 * @description
 */
public interface Sort {
    String getProperty();

    Boolean getAsc();

    void setProperty(String property);

    void setAsc(Boolean asc);
}
