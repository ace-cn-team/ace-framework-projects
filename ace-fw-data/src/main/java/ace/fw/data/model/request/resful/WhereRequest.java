package ace.fw.data.model.request.resful;

import ace.fw.data.model.GenericWhere;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/9 10:48
 * @description
 */
public class WhereRequest extends GenericWhere<WhereRequest, String> {
    public static WhereRequest build() {
        return new WhereRequest();
    }
}
