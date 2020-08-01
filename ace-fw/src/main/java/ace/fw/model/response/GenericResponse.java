package ace.fw.model.response;

import ace.fw.enums.SystemCodeEnum;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author caspar
 * @contact 279397942@qq.com
 * @date 2017/1/19
 * @description 通用出参数对象
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse<T> {
    /**
     * 返回结果
     */
    private T data;
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