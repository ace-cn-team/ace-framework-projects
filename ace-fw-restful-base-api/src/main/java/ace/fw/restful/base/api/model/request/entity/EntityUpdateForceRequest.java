package ace.fw.restful.base.api.model.request.entity;

import ace.fw.restful.base.api.model.where.Where;
import ace.fw.restful.base.api.model.where.impl.AbstractWhere;
import ace.fw.restful.base.api.model.where.impl.EntityWhereImpl;
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
public class EntityUpdateForceRequest<T> {
    @NotNull
    private T entity;
    private EntityWhereImpl where;
}