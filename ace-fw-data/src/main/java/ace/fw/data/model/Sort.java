package ace.fw.data.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:35
 * @description
 */
@Data
@Accessors(chain = true)
public class Sort {
    private String field;

    private Boolean asc;
}
