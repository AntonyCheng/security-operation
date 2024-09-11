package top.sharehome.securityoperation.controller.project;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.sharehome.securityoperation.common.base.Constants;
import top.sharehome.securityoperation.common.base.R;
import top.sharehome.securityoperation.common.validate.PostGroup;
import top.sharehome.securityoperation.config.log.annotation.ControllerLog;
import top.sharehome.securityoperation.config.log.enums.Operator;
import top.sharehome.securityoperation.model.dto.project.AdminProjectAddDto;
import top.sharehome.securityoperation.model.dto.project.AdminProjectPageDto;
import top.sharehome.securityoperation.model.dto.projectUser.AdminProjectUserAddDto;
import top.sharehome.securityoperation.model.page.PageModel;
import top.sharehome.securityoperation.model.vo.project.AdminProjectPageVo;
import top.sharehome.securityoperation.service.ProjectService;

/**
 * 管理员或项目经理管理项目控制器
 *
 * @author AntonyCheng
 */
@RestController
@RequestMapping("/admin/project")
@SaCheckLogin
public class AdminManagerProjectController {

    @Resource
    private ProjectService projectService;

    /**
     * 管理员/项目经理分页查询项目信息
     *
     * @param adminProjectPageDto 项目信息查询条件
     * @param pageModel           分页模型
     * @return 分页查询结果
     */
    @GetMapping("/page")
    @ControllerLog(description = "管理员/项目经理查询项目信息", operator = Operator.QUERY)
    @SaCheckRole(value = {Constants.ROLE_ADMIN, Constants.ROLE_MANAGER}, mode = SaMode.OR)
    public R<Page<AdminProjectPageVo>> pageProject(AdminProjectPageDto adminProjectPageDto, PageModel pageModel) {
        Page<AdminProjectPageVo> page = projectService.adminPageProject(adminProjectPageDto, pageModel);
        return R.ok(page);
    }

    /**
     * 项目经理添加项目信息
     *
     * @param adminProjectAddDto 被添加项目信息
     * @return 添加结果
     */
    @PostMapping("/add")
    @ControllerLog(description = "项目经理添加项目信息", operator = Operator.INSERT)
    @SaCheckRole(value = {Constants.ROLE_MANAGER})
    public R<String> addProject(@RequestBody @Validated({PostGroup.class}) AdminProjectAddDto adminProjectAddDto) {
        projectService.adminAddProject(adminProjectAddDto);
        return R.ok("添加成功");
    }

    /**
     * 项目经理添加项目成员
     *
     * @param adminProjectUserAddDto 被添加项目成员信息
     * @return 添加结果
     */
    @PostMapping("/add/user")
    @ControllerLog(description = "项目经理添加项目成员", operator = Operator.INSERT)
    @SaCheckRole(value = {Constants.ROLE_MANAGER})
    public R<String> addProjectUser(@RequestBody @Validated({PostGroup.class}) AdminProjectUserAddDto adminProjectUserAddDto) {
        projectService.adminAddProjectUser(adminProjectUserAddDto);
        return R.ok("添加成功");
    }

}
