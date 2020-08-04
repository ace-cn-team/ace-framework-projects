package ace.fw.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
    private String field;

    private Boolean asc;
}
