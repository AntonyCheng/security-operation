package top.sharehome.securityoperation.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 管理员列表查询用户信息Dto类
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdminUserListDto implements Serializable {

    /**
     * 用户工号
     */
    private String workId;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户角色
     */
    private String role;

    /**
     * 用户状态
     */
    private Integer state;

    @Serial
    private static final long serialVersionUID = -206041784748523505L;

}
