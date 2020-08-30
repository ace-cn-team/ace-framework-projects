package ace.fw.restful.base.api.model.entity;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/26 17:34
 * @description 支持序列化的 Function
 */
@FunctionalInterface
public interface EntityPropertyFunction<T, R> extends Function<T, R>, Serializable {
}
