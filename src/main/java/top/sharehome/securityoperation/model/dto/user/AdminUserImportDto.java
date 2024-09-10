package top.sharehome.securityoperation.model.dto.user;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.sharehome.securityoperation.common.validate.PostGroup;
import top.sharehome.securityoperation.common.validate.PutGroup;

import java.io.Serial;
import java.io.Serializable;

import static top.sharehome.securityoperation.common.base.Constants.REGEX_NUMBER;
import static top.sharehome.securityoperation.common.base.Constants.REGEX_NUMBER_AND_LETTER;

/**
 * 管理员添加用户Dto类
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserImportDto implements Serializable {

    /**
     * 工号
     */
    @ExcelProperty(value = "用户工号")
    private String workId;

    /**
     * 账号
     */
    @ExcelProperty(value = "用户账号")
    private String account;

    /**
     * 密码
     */
    @ExcelIgnore
    private String password;

    /**
     * 邮箱
     */
    @ExcelProperty(value = "用户邮箱")
    private String email;

    /**
     * 名称
     */
    @ExcelProperty(value = "用户名称")
    private String name;

    @Serial
    private static final long serialVersionUID = -6781445724058195731L;

}
