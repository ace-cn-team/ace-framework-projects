package ace.fw.data.model.request;

import ace.fw.data.model.OrderBy;
import ace.fw.data.model.PageRequest;
import ace.fw.data.model.Select;
import ace.fw.data.model.request.resful.WhereRequest;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/20 11:08
 * @description
 */
@Data
@Accessors(chain = true)
public class QueryRequest {
    private Select select;
    private WhereRequest where;
    private PageRequest page;
    private OrderBy orderBy;
}
