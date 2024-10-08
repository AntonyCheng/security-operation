package top.sharehome.securityoperation.model.dto.user;

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
import top.sharehome.securityoperation.config.encrypt.annotation.RSAEncrypt;

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
@Accessors(chain = true)
public class AdminUserAddDto implements Serializable {

    /**
     * 工号
     */
    @Size(min = 7, max = 7, message = "工号长度为7位", groups = {PostGroup.class})
    @NotBlank(message = "工号不能为空", groups = {PostGroup.class})
    @Pattern(regexp = REGEX_NUMBER, message = "工号仅由数字构成", groups = {PostGroup.class})
    private String workId;

    /**
     * 账号
     */
    @Size(min = 2, max = 16, message = "账号长度介于2-16位之间", groups = {PostGroup.class})
    @NotBlank(message = "账号不能为空", groups = {PostGroup.class})
    @Pattern(regexp = REGEX_NUMBER_AND_LETTER, message = "用户账号包含特殊字符", groups = {PostGroup.class})
    private String account;

    /**
     * 密码
     */
    @Size(min = 5, max = 16, message = "密码长度介于5-16位之间", groups = {PostGroup.class})
    @NotBlank(message = "密码不能为空", groups = {PostGroup.class})
    @RSAEncrypt
    private String password;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式错误", groups = {PostGroup.class})
    @NotBlank(message = "邮箱不能为空", groups = {PostGroup.class})
    private String email;

    /**
     * 名称
     */
    @Size(min = 1, max = 16, message = "名称长度介于1-16位之间", groups = {PostGroup.class})
    @NotBlank(message = "名称不能为空", groups = {PostGroup.class})
    private String name;

    @Serial
    private static final long serialVersionUID = 3927650956290675182L;

}
