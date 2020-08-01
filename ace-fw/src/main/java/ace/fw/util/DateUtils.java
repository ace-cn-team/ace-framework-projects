package ace.fw.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author caspar
 * @description 日期工具类，提供有关日期操作方面的方法。
 */
@Slf4j
public class DateUtils {

    public final static String FROMAT_yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public final static String FROMAT_yyyyMMddHHmmssSS = "yyyyMMddHHmmssSS";
    /**
     * 时间格式
     */
    public final static String FORMAT_HH_mm_ss_SS = "HH:mm:ss:SS";

    /**
     * 缺省短日期格式
     */
    public final static String FORMAT_yyyy_MM_dd = "yyyy-MM-dd";

    /**
     * yyyy-MM-dd HH:mm:ss格式数据。
     */
    public final static String FORMAT_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm格式数据。
     */
    public final static String FORMAT_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";


    /**
     * yyyy-MM-dd格式数据。
     */
    public final static String FROMAT_yyyy_MM_dd = "yyyy-MM-dd";

    /**
     * 取得指定日期所在周的第一天
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 取得指定日期所在周的最后一天
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }


    /**
     * <p>
     * 取得当前日期，并将其转换成格式为"dateFormat"的字符串 例子：假如当前日期是 2003-09-24 9:19:10，则：
     * <p>
     * <pre>
     * getCurrDateStr("yyyyMMdd")="20030924"
     * getCurrDateStr("yyyy-MM-dd")="2003-09-24"
     * getCurrDateStr("yyyy-MM-dd HH:mm:ss")="2003-09-24 09:19:10"
     * </pre>
     * <p>
     * </p>
     *
     * @param dateFormat String 日期格式字符串
     * @return String
     */
    public static String getNowFormat(String dateFormat) {
        return format(new Date(), dateFormat);
    }

    /**
     * @param date 2013-11-07 14:14:14
     * @return 20131107141414
     */
    public static String format(Date date, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.format(date);
        } catch (Throwable ex) {
            log.error("format fail", ex);
            throw new RuntimeException(ex);
        }
    }


    public static String format(long milliseconds, String pattern) {
        if (milliseconds <= 0) {
            return StringUtils.EMPTY;
        }


        if (StringUtils.isEmpty(pattern)) {
            pattern = FORMAT_yyyy_MM_dd_HH_mm_ss;
        }
        return format(new Date(milliseconds), pattern);
    }
}