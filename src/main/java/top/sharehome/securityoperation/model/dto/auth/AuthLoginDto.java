package top.sharehome.securityoperation.model.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.sharehome.securityoperation.common.validate.PostGroup;
import top.sharehome.securityoperation.config.captcha.model.Captcha;
import top.sharehome.securityoperation.config.encrypt.annotation.RSAEncrypt;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录Dto类
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AuthLoginDto implements Serializable {

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空", groups = {PostGroup.class})
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {PostGroup.class})
    @RSAEncrypt
    private String password;

    /**
     * 验证码参数实体类
     */
    private Captcha captcha;

    @Serial
    private static final long serialVersionUID = -2121896284587465661L;

}