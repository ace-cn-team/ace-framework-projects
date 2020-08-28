package ace.fw.restful.base.api.model.request.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/28 18:42
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityGetById<IdType> {
    @NotNull(message = "id不能为空")
    private IdType id;
}
