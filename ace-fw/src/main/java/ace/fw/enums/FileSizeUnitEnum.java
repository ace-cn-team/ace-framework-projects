package ace.fw.enums;

import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2019/12/16 15:03
 * @description 计算机文件大小单位
 */
public enum FileSizeUnitEnum implements BaseEnum<String> {
    BYTE("byte", "byte", 0L),
    KB("kb", "KB", 1L),
    MB("mb", "MB", 2L),
    GB("gb", "GB", 3L),
    TB("tb", "TB", 4L);
    @Getter
    private String code;
    @Getter
    private String desc;
    @Getter
    private Long baseUnitLevel;

    FileSizeUnitEnum(String code, String desc, Long baseUnitLevel) {
        this.code = code;
        this.desc = desc;
        this.baseUnitLevel = baseUnitLevel;
    }
}
