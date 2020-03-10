package ace.fw.enums;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/16 15:27
 * @description 基础枚举
 */
public interface BaseEnum<T> {
    /**
     * 编码
     *
     * @return
     */
    T getCode();

    /**
     * 描述
     *
     * @return
     */
    String getDesc();
}
