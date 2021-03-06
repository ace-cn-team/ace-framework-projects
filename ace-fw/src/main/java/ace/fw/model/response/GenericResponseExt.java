package ace.fw.model.response;

import ace.fw.enums.SystemCodeEnum;
import ace.fw.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

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
public class GenericResponseExt<T> {
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

    /**
     * 成功，则返回数据,失败则丢出业务异常信息
     *
     * @return
     */
    public T check() {
        if (this.getSuccess() == false) {
            throw new BusinessException(this.getCode(), this.getMessage());
        }
        return this.getData();
    }

    public boolean getSuccess() {
        return StringUtils.equals(SystemCodeEnum.SUCCESS.getCode(), this.getCode());
    }
}