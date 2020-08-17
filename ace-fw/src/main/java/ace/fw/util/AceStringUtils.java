package ace.fw.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/14 16:56
 * @description
 */
public class AceStringUtils {
    /**
     * 替换字符串
     *
     * @param text
     * @param params
     * @return
     */
    public static String replaceEach(String text, Map<String, String> params) {
        String[] keys = params.keySet().toArray(new String[params.keySet().size()]);
        String[] values = params.values().toArray(new String[params.values().size()]);
        return StringUtils.replaceEach(text, keys, values);
    }

    public static String replaceEach(String text, Pair<String, String>... params) {
        String[] keys = Stream.of(params).map(p -> p.getKey()).toArray(String[]::new);
        String[] values = Stream.of(params).map(p -> p.getValue()).toArray(String[]::new);
        return StringUtils.replaceEach(text, keys, values);
    }

    public static void main(String[] args) {

        replaceEach("",
                Pair.of("", ""),
                Pair.of("", ""),
                Pair.of("", "")

        );
    }
}