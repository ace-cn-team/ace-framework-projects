package ace.fw.restful.base.api.model.orderby.impl;

import ace.fw.restful.base.api.model.orderby.Sort;
import ace.fw.restful.base.api.model.orderby.OrderBy;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 15:50
 * @description
 */
@Data
public abstract class AbstractOrderBy<TOrderBy extends AbstractOrderBy> implements OrderBy<TOrderBy> {
    private List<Sort> sorts = new ArrayList(10);

    public TOrderBy add(String propertyName, boolean asc) {
        sorts.add(Sort.builder()
                .asc(asc)
                .property(propertyName)
                .build()
        );
        return self();
    }

    @Override
    public TOrderBy asc(String propertyName) {
        this.add(propertyName, true);
        return self();
    }

    @Override
    public TOrderBy desc(String propertyName) {
        this.add(propertyName, false);
        return self();
    }
}
