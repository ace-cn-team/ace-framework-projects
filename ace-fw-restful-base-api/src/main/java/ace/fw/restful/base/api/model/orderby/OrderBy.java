package ace.fw.restful.base.api.model.orderby;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 15:50
 * @description
 */
public interface OrderBy<TOrderBy extends OrderBy> {

    List<Sort> getSorts();

    TOrderBy add(String propertyName, boolean asc);

    TOrderBy asc(String propertyName);

    TOrderBy desc(String propertyName);

    default TOrderBy self() {
        return (TOrderBy) this;
    }
}
