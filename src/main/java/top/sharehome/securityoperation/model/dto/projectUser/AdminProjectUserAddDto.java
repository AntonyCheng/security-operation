package top.sharehome.securityoperation.model.dto.projectUser;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.sharehome.securityoperation.common.validate.PostGroup;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 管理员添加项目用户Dto类
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdminProjectUserAddDto implements Serializable {

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空", groups = {PostGroup.class})
    private Long projectId;

    /**
     * 项目成员用户ID列表
     */
    @NotEmpty(message = "项目成员列表不能为空", groups = {PostGroup.class})
    private List<Long> userIds;

    @Serial
    private static final long serialVersionUID = -1136724328066203789L;

}
