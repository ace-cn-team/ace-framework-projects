package ace.fw.restful.base.api.model.request.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:29
 * @description 分页参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest {
    /**
     * sql select 语句，默认：*
     */
    private SelectRequest select;
    /**
     * sql where 语句，默认：空
     */
    private WhereRequest where;
    /**
     * sql order by 语句，不能为空
     */
    @NotNull
    private OrderByRequest orderBy;
    /**
     * sql 分页方法，根据底层实现进行分页
     */
    @NotNull
    private PagerRequest pager;
}
