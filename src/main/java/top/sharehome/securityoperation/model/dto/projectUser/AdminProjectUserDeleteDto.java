package top.sharehome.securityoperation.model.dto.projectUser;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.sharehome.securityoperation.common.validate.DeleteGroup;
import top.sharehome.securityoperation.common.validate.PostGroup;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 管理员删除项目用户Dto类
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdminProjectUserDeleteDto implements Serializable {

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空", groups = {DeleteGroup.class})
    private Long projectId;

    /**
     * 项目成员用户ID列表
     */
    @NotNull(message = "项目成员ID不能为空", groups = {DeleteGroup.class})
    private Long userId;

    @Serial
    private static final long serialVersionUID = -8532742738369165759L;

}
