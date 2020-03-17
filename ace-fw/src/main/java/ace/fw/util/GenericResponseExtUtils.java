package ace.fw.util;


import ace.fw.enums.SystemCodeEnum;
import ace.fw.exception.BusinessException;
import ace.fw.model.response.GenericResponseExt;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/16 15:58
 * @description {@link GenericResponseExt}
 */
public final class GenericResponseExtUtils {

    @Builder
    private static <T> GenericResponseExt<T> genericResponseBuilder(T data, String code, String message) {
        GenericResponseExt<T> genericResponse = new GenericResponseExt();
        genericResponse.setCode(code);
        genericResponse.setData(data);
        genericResponse.setMessage(message);
        return genericResponse;
    }

    public static <T> GenericResponseExt<T> buildSuccessWithData(T data) {
        GenericResponseExtUtils.GenericResponseExtBuilder<T> builder = GenericResponseExtUtils.builder();
        return builder.code(SystemCodeEnum.SUCCESS.getCode())
                .message(SystemCodeEnum.SUCCESS.getDesc())
                .data(data)
                .build();
    }
    public static <T> GenericResponseExt<T> buildFailureWithData(T data) {
        GenericResponseExtUtils.GenericResponseExtBuilder<T> builder = GenericResponseExtUtils.builder();
        return builder.code(SystemCodeEnum.BUSINESS_EXCEPTION.getCode())
                .message(SystemCodeEnum.BUSINESS_EXCEPTION.getDesc())
                .data(data)
                .build();
    }
    /**
     * 根据系统代码创建返回结果
     *
     * @param systemCodeEnum
     * @return
     */
    public static GenericResponseExt buildBySystemCodeEnum(SystemCodeEnum systemCodeEnum) {
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
    public static <T> T getAndCheck(Supplier<GenericResponseExt<T>> supplier) {
        GenericResponseExt<T> genericResponse = supplier.get();
        if (isSuccess(genericResponse)) {
            return genericResponse.getData();
        }
        throw new BusinessException(genericResponse.getCode(), genericResponse.getMessage());
    }

    public static boolean isSuccess(GenericResponseExt genericResponse) {
        return StringUtils.equals(SystemCodeEnum.SUCCESS.getCode(), genericResponse.getCode());
    }
}
