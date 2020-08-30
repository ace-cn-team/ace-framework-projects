package ace.fw.restful.base.api.model.request.entity;


import ace.fw.restful.base.api.model.request.base.WhereRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/7 14:14
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityUpdateRequest<T> {
    @NotNull
    private T entity;
    private WhereRequest where;
}
