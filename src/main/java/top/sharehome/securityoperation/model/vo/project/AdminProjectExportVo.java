package top.sharehome.securityoperation.model.vo.project;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理员分页查询项目信息Vo类
 *
 * @author AntonyCheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminProjectExportVo implements Serializable {

    /**
     * 项目ID
     */
    @ExcelProperty(value = "项目ID")
    private Long id;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String name;

    /**
     * 项目描述
     */
    @ExcelProperty(value = "项目描述")
    private String description;

    /**
     * 项目仓库地址
     */
    @ExcelProperty(value = "项目仓库地址")
    private String url;

    /**
     * 项目经理用户
     */
    @ExcelProperty(value = "项目经理用户")
    private String managerName;

    /**
     * 项目成员信息JSON
     */
    @ExcelProperty(value = "项目成员信息[工号-名称-账号]")
    private String userInfo;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @Serial
    private static final long serialVersionUID = 7502941716002779415L;

}
