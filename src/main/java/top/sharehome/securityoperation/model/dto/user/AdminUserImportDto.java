package top.sharehome.securityoperation.model.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;
import top.sharehome.securityoperation.common.validate.PutGroup;

import java.io.Serial;
import java.io.Serializable;

/**
 * 管理员添加项目经理/项目经理添加用户Dto类
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdminUserImportDto implements Serializable {

    /**
     * 用户信息文件（大小：20MB；格式：xls或xlsx）
     */
    @NotNull(message = "文件不能为空", groups = {PutGroup.class})
    private MultipartFile file;

    @Serial
    private static final long serialVersionUID = 5940536388771724739L;

}
