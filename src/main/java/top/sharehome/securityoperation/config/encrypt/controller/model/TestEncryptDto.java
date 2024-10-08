package top.sharehome.securityoperation.config.encrypt.controller.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.sharehome.securityoperation.common.validate.PostGroup;
import top.sharehome.securityoperation.config.encrypt.annotation.RSAEncrypt;

import java.io.Serial;
import java.io.Serializable;

/**
 * 测试加密Dto类
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TestEncryptDto implements Serializable {

    @Size(min = 5, max = 10, message = "账号长度介于5-10之间", groups = {PostGroup.class})
    private String account;

    @Size(min = 5, max = 10, message = "密码长度介于5-10之间", groups = {PostGroup.class})
    @RSAEncrypt
    private String password;

    @Serial
    private static final long serialVersionUID = 1524087173502021198L;

}
