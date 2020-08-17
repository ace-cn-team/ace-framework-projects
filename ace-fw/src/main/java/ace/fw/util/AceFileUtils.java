package ace.fw.util;

import ace.fw.enums.FileSizeUnitEnum;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/14 18:35
 * @description
 */
public class AceFileUtils {
    private final static int BASE_UNIT = 1024;

    /**
     * 验证文件大小
     *
     * @param fileSizeByte      文件大小，单位byte
     * @param limitSize         文件限制大小
     * @param limitSizeUnitEnum 文件限制大小的单位
     * @return
     */
    public static boolean validFileSize(double fileSizeByte, double limitSize, FileSizeUnitEnum limitSizeUnitEnum) {
        double fileSize = fileSizeByte / (Math.pow(BASE_UNIT, limitSizeUnitEnum.getBaseUnitLevel()));

        return fileSize <= limitSize;
    }

    /**
     * 验证文件后缀名是否相等
     *
     * @param fileName            文件名称
     * @param expectFileExtension 期望的文件后缀
     * @return
     */
    public static boolean validFileExtensionEqualsIgnoreCase(String fileName, String expectFileExtension) {
        String fileExtension = FilenameUtils.getExtension(fileName);

        return StringUtils.equalsIgnoreCase(fileExtension, expectFileExtension);
    }

    public static void main(String[] args) {
        System.out.println(validFileSize(4, 3, FileSizeUnitEnum.BYTE));
    }
}
