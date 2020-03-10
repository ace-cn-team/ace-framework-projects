package ace.fw.data.model.request.resful;

import ace.fw.data.model.GenericWhere;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/9 10:48
 * @description
 */
@Data
@Accessors(chain = true)
public class WhereRequest extends GenericWhere<String> {

}
