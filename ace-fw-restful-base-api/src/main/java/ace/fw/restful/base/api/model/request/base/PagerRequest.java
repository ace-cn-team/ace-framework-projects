package ace.fw.restful.base.api.model.request.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:29
 * @description 分页入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagerRequest {
    /**
     * 第几页
     */
    private Integer pageIndex = 0;
    /**
     * 分页大小
     */
    private Integer pageSize = 10;
}
