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
 * 任务反馈类
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_feedback")
@Accessors(chain = true)
public class Feedback implements Serializable {
    /**
     * 反馈ID
     */
    @TableId(value = "feedback_id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 反馈所属任务ID
     */
    @TableField(value = "feedback_task_id")
    private Long taskId;

    /**
     * 反馈所属任务被分配用户ID
     */
    @TableField(value = "feedback_user_id")
    private Long userId;

    /**
     * 反馈内容
     */
    @TableField(value = "feedback_content")
    private String content;

    /**
     * 反馈附件路径
     */
    @TableField(value = "feedback_url")
    private String url;

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
    private static final long serialVersionUID = 6528618359013830600L;
}