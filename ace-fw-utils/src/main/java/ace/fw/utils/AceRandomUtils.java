package ace.fw.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class AceRandomUtils {

    /** 年月日时分秒(无下划线) yyMMddHHmmss */
    public static final String DATE_FORMAT = "yyMMddHHmmssSSS";

    /**
     * 获取count个随机数
     * @param count 随机数个数
     * @return
     */
    public static String randomNumber(int count) {
        StringBuffer sb = new StringBuffer();
        String str = "0123456789";
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            int num = r.nextInt(str.length());
            sb.append(str.charAt(num));
            str = str.replace((str.charAt(num) + ""), "");
        }
        return sb.toString();
    }

    /**
     * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
     * @return
     *      以yyMMddHHmmss为格式的当前系统时间+5位随机数
     */
    public static String getOrderSn() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        String sn = df.format(date);
        sn += randomNumber(5);
        return sn;
    }

    /**
     * 生成密码盐值
     */
    public static synchronized String genFixLengthRandom(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer(length);

        for(int i = 0; i < length; ++i) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
