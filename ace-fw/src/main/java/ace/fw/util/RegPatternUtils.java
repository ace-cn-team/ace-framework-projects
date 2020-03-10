package ace.fw.util;

import ace.fw.model.response.GenericResponseExt;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2017/12/16 15:58
 * @description
 */
public final class RegPatternUtils {
    public static final String UUID = "^[A-Fa-f0-9]{8}(-?[A-Fa-f0-9]{4}){3}-?[A-Fa-f0-9]{12}$";
    public static final String SQL_COUNT_REPLACE = "(?i)orders\\s*(?i)BY\\s*.*";
    public static final String ORDER_STATUS = "1|2|3|4|5";
    public static final String DELETE_FLAG = "0|1";
    public static final String AMOUNT = "^[1-9][0-9]{0,16}$";
    public static final String FEE = "^[0-9]{1,17}$";
    public static final String CURRENCY_CODE = "^(?:RMB|USD|HKD|VTL)$";
    public static final String REG_BIRTH_DAY = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
    public static final String SIMPLE_DATE = "^\\d{4}((0[1-9])|(1[0-2]))(([0-2][0-9])|(3[0-1]))$";
    public static final String SIMPLE_TIME = "^([0-1][0-9]|[2][0-3])([0-5][0-9]){2}$";
    public static final String SIMPLE_DATE_TIME = "^\\d{4}((0[1-9])|(1[0-2]))(([0-2][0-9])|(3[0-1]))([0-1][0-9]|[2][0-3])([0-5][0-9]){2}$";
    public static final String SIMPLE_DATE_TIME_SPLIT = "^\\d{4}-((0[1-9])|(1[0-2]))-(([0-2][0-9])|(3[0-1])) ([0-1][0-9]|[2][0-3])(:[0-5][0-9]){2}$";
    public static final String REG_YYYYMM = "^\\d{4}((0[1-9])|(1[0-2]))$";
    public static final String REG_YYYYMMDD = "^\\d{4}((0[1-9])|(1[0-2]))(([0-2][0-9])|(3[0-1]))$";
    public static final String REG_YYYY_MM_DD = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
    public static final String REG_MOBILE = "^1\\d{10}$";
    public static final String REG_EMAIL = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
    public static final String REG_PHONE = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
    public static final String REG_DIGIT = "^[1-9]\\d*|{0}$";
    public static final String REG_POSITIVE_INTEGER = "^\\+?[1-9][0-9]*$";
    public static final String REG_DECIMALS = "\\-?[1-9]\\d+(\\.\\d+)?";
    public static final String REG_BLANK_SPACE = "\\s+";
    public static final String REG_CHINESE = "^[一-龥]+$";
    public static final String REG_URL = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
    public static final String REG_DOMAIN = "(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)";
    public static final String REG_POST_CODE = "[1-9]\\d{5}";
    public static final String REG_IP_ADDRESS = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
    public static final String REG_VERSION = "^\\d+(\\.\\d+){1,2}$";
    public static final String REG_IDCARD = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
    /**
     * 正则验证6位数字
     */
    public static final String SIX_DIGIT = "^\\d{6}$";

    /**
     * 0或者1标识
     */
    public static final String ZERO_OR_ONE = "0|1";

    public static boolean isMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return false;
        }
        return mobile.matches(REG_MOBILE);
    }
}