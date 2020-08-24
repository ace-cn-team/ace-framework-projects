package ace.fw.mybatis.plus.extension.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/24 14:01
 * @description
 */
@Data
@Builder
public class EntityField {
    private Field field;
    private boolean version;
    private String columnName;

    public EntityField(Field field, boolean version) {
        this.field = field;
        this.version = version;
    }

    public EntityField(Field field, boolean version, String columnName) {
        this.field = field;
        this.version = version;
        this.columnName = columnName;
    }
}
