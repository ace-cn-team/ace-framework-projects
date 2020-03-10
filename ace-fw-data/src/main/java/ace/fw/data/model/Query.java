package ace.fw.data.model;

import ace.fw.data.model.OrderBy;
import ace.fw.data.model.PageRequest;
import ace.fw.data.model.Where;
import ace.fw.data.model.Select;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 15:48
 * @description
 */
@Data
@Accessors(chain = true)
public class Query {
    private Select select;
    private Where where;
    private OrderBy orderBy;
    private PageRequest page;
}
