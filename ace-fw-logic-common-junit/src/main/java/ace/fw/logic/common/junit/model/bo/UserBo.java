package ace.fw.logic.common.junit.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/29 15:39
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBo {
    private String id;
    private Integer state;
    private Integer version;
    private String nickName;
    private LocalDateTime createTime;
}
