package ace.fw.data.model.request.resful.uniform;

import ace.fw.data.model.request.resful.WhereRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.util.Map;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/7 14:14
 * @description
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniformUpdateForceRequest {
    @Size(min = 1)
    private Map<String, String> entity;
    private WhereRequest where;
}
