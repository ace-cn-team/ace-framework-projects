package ace.fw.restful.base.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:35
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sort {
    /**
     * 属性名称
     */
    private String property;
    /**
     * 排序方向
     */
    private Boolean asc;
}
