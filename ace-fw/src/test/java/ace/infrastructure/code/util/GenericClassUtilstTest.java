package ace.infrastructure.code.util;

import ace.fw.util.GenericClassUtils;
import ace.fw.util.GenericResponseUtils;
import ace.fw.util.ReflectionUtils;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/11 16:10
 * @description
 */
public class GenericClassUtilstTest {
    public static void main(String[] args) {
        Class<String> cls = new GenericClassUtils<String>() {
        }.getGenericClass();
        Class<Integer> cls1 = new GenericClassUtils<Integer>() {
        }.getGenericClass();
        int i = 0;
    }

}
