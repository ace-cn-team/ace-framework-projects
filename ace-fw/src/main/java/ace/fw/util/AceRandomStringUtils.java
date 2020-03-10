package ace.fw.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/3/9 10:27
 * @description
 */
public final class AceRandomStringUtils {
    private final static char[] DEFAULT_ALPHA_NUMERIC_CHAR = "abcde2345678gfynmnpwx".toCharArray();

    public static String randomAlphanumeric(int verifyCodeCount) {
        return RandomStringUtils.random(verifyCodeCount, 0, DEFAULT_ALPHA_NUMERIC_CHAR.length, true, true, DEFAULT_ALPHA_NUMERIC_CHAR);
    }

    public static String randomAlphabetic(int verifyCodeCount) {
        return RandomStringUtils.randomAlphabetic(verifyCodeCount);
    }

    public static String randomNumeric(int verifyCodeCount) {
        return RandomStringUtils.randomNumeric(verifyCodeCount);
    }
}
