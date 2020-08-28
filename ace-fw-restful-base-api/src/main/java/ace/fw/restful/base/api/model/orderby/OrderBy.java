package ace.fw.restful.base.api.model.orderby;

import ace.fw.restful.base.api.model.Sort;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 15:50
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderBy<TOrderBy extends OrderBy> {
    @Size(min = 1)
    private List<Sort> sorts = new ArrayList(10);

    public TOrderBy add(String propertyName, boolean asc) {
        sorts.add(Sort.builder()
                .asc(asc)
                .property(propertyName)
                .build()
        );
        return self();
    }

    protected TOrderBy self() {
        return (TOrderBy) this;
    }
}
