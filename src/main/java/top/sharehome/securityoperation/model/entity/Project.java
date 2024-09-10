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
 * 项目类
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_project")
@Accessors(chain = true)
public class Project implements Serializable {

    /**
     * 项目ID
     */
    @TableId(value = "project_id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目名称
     */
    @TableField(value = "project_name")
    private String name;

    /**
     * 项目描述
     */
    @TableField(value = "project_description")
    private String description;

    /**
     * 项目仓库地址
     */
    @TableField(value = "project_url")
    private String url;

    /**
     * 项目经理用户ID
     */
    @TableField(value = "project_manager_id")
    private Long managerId;

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
    private static final long serialVersionUID = -4832471005520424359L;

}