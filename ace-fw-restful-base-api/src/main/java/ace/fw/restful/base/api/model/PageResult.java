package ace.fw.restful.base.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:37
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    /**
     * 返回数据
     */
    private List<T> data = new ArrayList<>(10);
    /**
     * 接口返回信息 总记录数
     */
    private long totalCount = 0;
    /**
     * 接口返回信息 总页数
     */
    private long totalPage = 0;
}
