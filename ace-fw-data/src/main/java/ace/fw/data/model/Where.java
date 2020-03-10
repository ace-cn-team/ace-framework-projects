package ace.fw.data.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 14:27
 * @description
 */
@Data
@Accessors(chain = true)
public class Where extends GenericWhere<Object> {

}
