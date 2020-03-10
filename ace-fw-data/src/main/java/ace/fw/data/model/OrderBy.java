package ace.fw.data.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 15:50
 * @description
 */
@Data
@Accessors(chain = true)
public class OrderBy {
    private List<Sort> sorts = new ArrayList();
}
