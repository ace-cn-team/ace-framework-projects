package ace.fw.restful.base.api.model.request.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/30 21:53
 * @description 查询参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindRequest {
    private SelectRequest select;
    @NotNull
    private WhereRequest where;
}
