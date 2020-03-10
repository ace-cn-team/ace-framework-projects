package ace.fw.model.response;

import ace.fw.enums.SystemCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author 陈志杭
 * @contact 279397942@qq.com
 * @date 2017/1/19
 * @description 通用出参数对象
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {

    /**
     * 返回代码
     * {@link SystemCodeEnum}
     */
    private String code;
    /**
     * 返回消息
     */
    private String message;

}