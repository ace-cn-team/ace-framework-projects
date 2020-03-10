package ace.fw.data.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:29
 * @description 分页入参
 */
@Data
@Accessors(chain = true)
public class PageRequest {
    /**
     * 第几页
     */

    private Integer pageIndex = 0;
    /**
     * 分页大小
     */
    private Integer pageSize = 10;
}
