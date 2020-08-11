package ace.fw.json.jackson.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/11 11:36
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    private String string;
    private LocalDateTime now;
    private Integer integer;
    private Double aDouble;
    private String nickName;
}