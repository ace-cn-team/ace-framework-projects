package ace.fw.utils.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class WebUtils {
    private static final String regValidatorIp = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";


    public static String getURL(HttpServletRequest request) {
        String contextPath = request.getContextPath().equals("/") ? "" : request.getContextPath();
        String url = "http://" + request.getServerName();
        if (null2Int(Integer.valueOf(request.getServerPort())) != 80) {
            url = url + ":" + null2Int(Integer.valueOf(request.getServerPort())) + contextPath;
        } else {
            url = url + contextPath;
        }
        return url;
    }

    public static int null2Int(Object s) {
        int v = 0;
        if (s != null) {
            try {
                v = Integer.parseInt(s.toString());
            } catch (Exception localException) {
            }
        }
        return v;
    }

    /**
     * requeset所有参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> getParamMap(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        if (params == null)
            return null;
        Map<String, String> queryMap = new HashMap<String, String>();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            if (entry.getValue() == null || entry.getValue().length <= 0)
                continue;
            queryMap.put(entry.getKey(), entry.getValue()[0]);
        }
        return queryMap;
    }


    /**
     * 获取请求的IP地址
     *
     * @return
     */
    public static String getIpAddr() {
        javax.servlet.http.HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        // 如果客户端经过多级反向代理，则X-Forwarded-For的值并不止一个，而是一串IP值，
        // 取X-Forwarded-For中第一个非unknown的有效IP字符串即为用户真实IP
        String ipResult = getIp(ip);
        if (ipResult != null) {
            return ipResult;
        }

        ip = request.getHeader("Proxy-Client-IP");
        ipResult = getIp(ip);
        if (ipResult != null) {
            return ipResult;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        ipResult = getIp(ip);
        if (ipResult != null) {
            return ipResult;
        }
        ip = request.getRemoteAddr();
        ipResult = getIp(ip);
        if (ipResult != null) {
            return ipResult;
        }
        return "";
    }

    private static String getIp(String ips) {
        if (StringUtils.isEmpty(ips)) {
            return null;
        }
        String[] tokens = ips.split(",");
        for (String s : tokens) {
            if (isIp(s.trim())) {
                return s.trim();
            }
        }
        return null;
    }

    /**
     * 判断是否是有效的IP地址。
     *
     * @param value
     * @return
     */
    public static boolean isIp(String value) {
        if (StringUtils.isEmpty(value))
            return false;
        return value.matches(regValidatorIp);
    }
}
