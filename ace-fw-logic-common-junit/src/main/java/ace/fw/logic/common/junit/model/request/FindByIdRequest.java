package ace.fw.logic.common.junit.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/29 15:42
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindByIdRequest {
    private String id;
}
