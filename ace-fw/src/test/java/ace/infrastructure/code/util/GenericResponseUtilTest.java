package ace.infrastructure.code.util;

import ace.fw.util.GenericResponseUtils;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/16 16:44
 * @description
 */
public class GenericResponseUtilTest {
    public static void main(String[] args) {
        GenericResponseUtils.GenericResponseBuilder<Integer> builder = GenericResponseUtils.builder();

        builder.data(1)
                .message("a")
                .code("code")
                .build();
        System.out.println(builder.build().toString());
    }
}
