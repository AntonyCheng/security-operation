package top.sharehome.securityoperation.model.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理员列表查询用户信息Vo类
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdminUserListVo implements Serializable {

    /**
     * 用户ID
     */
    private Long id;

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
     * 用户角色（admin/manager/user）
     */
    private String role;

    @Serial
    private static final long serialVersionUID = -32286705786492867L;

}
