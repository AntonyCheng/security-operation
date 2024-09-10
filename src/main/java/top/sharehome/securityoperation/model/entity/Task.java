package top.sharehome.securityoperation.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务类
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_task")
@Accessors(chain = true)
public class Task implements Serializable {
    /**
     * 任务ID
     */
    @TableId(value = "task_id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 任务项目ID
     */
    @TableField(value = "task_project_id")
    private Long projectId;

    /**
     * 任务名称
     */
    @TableField(value = "task_name")
    private String name;

    /**
     * 任务描述
     */
    @TableField(value = "task_description")
    private String description;

    /**
     * 任务状态（0待分配1进行中2待审核3已完成4已关闭）
     */
    @TableField(value = "task_state")
    private Integer state;

    /**
     * 任务被分配用户ID
     */
    @TableField(value = "task_user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除（0表示未删除，1表示已删除）
     */
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer delFlag;

    @Serial
    private static final long serialVersionUID = -1275855219118827322L;
}