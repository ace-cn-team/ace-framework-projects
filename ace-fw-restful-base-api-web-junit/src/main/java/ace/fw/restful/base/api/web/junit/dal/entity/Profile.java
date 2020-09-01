package ace.fw.restful.base.api.web.junit.dal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/28 10:41
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "profile")
public class Profile {
    @TableId
    private String id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    @TableField(value = "state")
    private Integer state;
    @TableField(value = "level")
    private Long level;
    @Version
    @TableField(value = "row_version")
    private Integer rowVersion;
}
