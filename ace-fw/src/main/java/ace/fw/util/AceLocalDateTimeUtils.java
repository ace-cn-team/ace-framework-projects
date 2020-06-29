package ace.fw.util;

import java.time.LocalDateTime;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/6/28 18:40
 * @description
 */
public final class AceLocalDateTimeUtils {
    public static final LocalDateTime MIN_MYSQL = LocalDateTime.of(0000, 1, 1, 0, 0, 0);
    public static final LocalDateTime MAX_MYSQL = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
}
