package ace.fw.data.model.request.resful;

import ace.fw.data.model.GenericCondition;
import ace.fw.data.model.Sort;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/14 15:19
 * @description
 */
@Data
@Accessors(chain = true)
public class PageQueryRequest {
    private List<String> fields = new ArrayList<>();
    private List<GenericCondition<String>> conditions = new ArrayList<>();
    /**
     * 第几页
     */
    private Integer pageIndex = 0;
    /**
     * 分页大小
     */
    private Integer pageSize = 10;
    private List<Sort> sorts = new ArrayList();
}
