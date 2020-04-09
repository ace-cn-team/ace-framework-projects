package ace.fw.mq.serializer;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/8 18:07
 * @description
 */
@FunctionalInterface
public interface Deserializer<T> {
    T deserialize(byte[] bytes);
}
