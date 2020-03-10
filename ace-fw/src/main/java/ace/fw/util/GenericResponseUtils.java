package ace.fw.util;


import ace.fw.enums.SystemCodeEnum;
import ace.fw.exception.BusinessException;
import ace.fw.model.response.GenericResponse;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/16 15:58
 * @description {@link GenericResponse}
 */
public final class GenericResponseUtils {

    @Builder
    private static <T> GenericResponse<T> genericResponseBuilder(T data, String code, String message) {
        GenericResponse<T> genericResponse = new GenericResponse();
        genericResponse.setCode(code);
        genericResponse.setData(data);
        genericResponse.setMessage(message);
        return genericResponse;
    }

    public static <T> GenericResponse<T> buildSuccessWithData(T data) {
        GenericResponseUtils.GenericResponseBuilder<T> builder = GenericResponseUtils.builder();
        return builder.code(SystemCodeEnum.Success.getCode())
                .message(SystemCodeEnum.Success.getDesc())
                .data(data)
                .build();
    }

    /**
     * 根据系统代码创建返回结果
     *
     * @param systemCodeEnum
     * @return
     */
    public static GenericResponse buildBySystemCodeEnum(SystemCodeEnum systemCodeEnum) {
        return builder()
                .code(systemCodeEnum.getCode())
                .message(systemCodeEnum.getDesc())
                .build();
    }

    /**
     * 成功则返回调用结果，否则  throw new BusinessException(genericResponse.getCode(), genericResponse.getMessage());
     *
     * @param supplier
     * @param <T>
     * @return
     */
    public static <T> T getAndCheck(Supplier<GenericResponse<T>> supplier) {
        GenericResponse<T> genericResponse = supplier.get();
        if (isSuccess(genericResponse)) {
            return genericResponse.getData();
        }
        throw new BusinessException(genericResponse.getCode(), genericResponse.getMessage());
    }

    public static boolean isSuccess(GenericResponse genericResponse) {
        return StringUtils.equals(SystemCodeEnum.Success.getCode(), genericResponse.getCode());
    }
}
