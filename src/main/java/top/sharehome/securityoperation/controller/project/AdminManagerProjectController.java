package top.sharehome.securityoperation.controller.project;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sharehome.securityoperation.common.base.Constants;
import top.sharehome.securityoperation.common.base.R;
import top.sharehome.securityoperation.config.log.annotation.ControllerLog;
import top.sharehome.securityoperation.config.log.enums.Operator;
import top.sharehome.securityoperation.model.page.PageModel;
import top.sharehome.securityoperation.service.ProjectService;

/**
 * 管理员/项目经理管理项目控制器
 *
 * @author AntonyCheng
 */
@RestController
@RequestMapping("/admin/project")
@SaCheckLogin
@SaCheckRole(value = {Constants.ROLE_ADMIN, Constants.ROLE_MANAGER}, mode = SaMode.OR)
public class AdminManagerProjectController {

    @Resource
    private ProjectService projectService;

//    /**
//     * 管理员/项目经理分页查询项目信息
//     *
//     * @param adminProjectPageDto 项目信息查询条件
//     * @param pageModel           分页模型
//     * @return 分页查询结果
//     */
//    @GetMapping("/page")
//    @ControllerLog(description = "管理员/项目经理查询项目信息", operator = Operator.QUERY)
//    public R<Page<AdminProjectPageVo>> pageProject(AdminProjectPageDto adminProjectPageDto, PageModel pageModel){
//        Page<AdminProjectPageVo> page = projectService.adminPageProject(adminProjectPageDto,pageModel);
//        return R.ok(page);
//    }

}
