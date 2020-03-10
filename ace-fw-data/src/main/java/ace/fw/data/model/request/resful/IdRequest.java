package ace.fw.data.model.request.resful;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/19 10:13
 * @description
 */
@Data
@Accessors(chain = true)
public class IdRequest {
    @NotBlank(message = "id不能为空")
    private String id;
}
