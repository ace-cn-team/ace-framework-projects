package ace.fw.restful.base.api.model.request.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

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
public class EntityGetListById<IdType> {
    @Size(min = 1, message = "id不能为空")
    private List<IdType> ids;
}
