package top.sharehome.securityoperation.model.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.sharehome.securityoperation.common.validate.PutGroup;

import java.io.Serial;
import java.io.Serializable;

/**
 * 管理员修改项目Dto类
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdminProjectUpdateDto implements Serializable {

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空", groups = {PutGroup.class})
    private Long id;

    /**
     * 项目名称
     */
    @Size(min = 1, max = 20, message = "项目名称长度介于1-20位之间", groups = {PutGroup.class})
    @NotBlank(message = "项目名称不能为空", groups = {PutGroup.class})
    private String name;

    /**
     * 项目描述
     */
    @Size(min = 1, max = 500, message = "项目描述长度介于1-500位之间", groups = {PutGroup.class})
    @NotBlank(message = "项目描述不能为空", groups = {PutGroup.class})
    private String description;

    /**
     * 项目仓库地址
     */
    @Pattern(regexp = "^(http://|https://)[\\w-]+(\\.[\\w-]+)+([\\w-.,@?^=%&:/~+#]*[\\w-@?^=%&/~+#])?$", message = "仓库地址格式错误", groups = {PutGroup.class})
    private String url;

    @Serial
    private static final long serialVersionUID = -7782693794316298344L;

}
