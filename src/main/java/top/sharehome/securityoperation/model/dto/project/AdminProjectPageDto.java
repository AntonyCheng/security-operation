package top.sharehome.securityoperation.model.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 管理员/项目经理分页查询项目信息Dto类
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdminProjectPageDto implements Serializable {

    /**
     * 项目名称（like）
     */
    private String name;

    /**
     * 项目描述（like）
     */
    private String description;

    /**
     * 项目仓库地址（like）
     */
    private String url;

    /**
     * 项目经理用户ID（eq）
     */
    private Long managerId;

    /**
     * 普通用户ID（eq）
     */
    private Long userId;

    @Serial
    private static final long serialVersionUID = -3758759431919087362L;

}
