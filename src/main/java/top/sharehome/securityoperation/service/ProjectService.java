package top.sharehome.securityoperation.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.securityoperation.model.dto.project.AdminProjectAddDto;
import top.sharehome.securityoperation.model.dto.project.AdminProjectPageDto;
import top.sharehome.securityoperation.model.dto.project.AdminProjectUpdateDto;
import top.sharehome.securityoperation.model.dto.projectUser.AdminProjectUserAddDto;
import top.sharehome.securityoperation.model.dto.projectUser.AdminProjectUserDeleteDto;
import top.sharehome.securityoperation.model.entity.Project;
import top.sharehome.securityoperation.model.page.PageModel;
import top.sharehome.securityoperation.model.vo.project.AdminProjectExportVo;
import top.sharehome.securityoperation.model.vo.project.AdminProjectPageVo;

import java.util.List;

/**
 * 项目服务接口
 *
 * @author AntonyCheng
 */
public interface ProjectService extends IService<Project> {

    /**
     * 管理员/项目经理分页查询项目信息
     *
     * @param adminProjectPageDto 项目信息查询条件
     * @param pageModel           分页模型
     * @return 分页查询结果
     */
    Page<AdminProjectPageVo> adminPageProject(AdminProjectPageDto adminProjectPageDto, PageModel pageModel);

    /**
     * 项目经理添加项目信息
     *
     * @param adminProjectAddDto 被添加项目信息
     */
    void adminAddProject(AdminProjectAddDto adminProjectAddDto);

    /**
     * 项目经理添加项目成员
     *
     * @param adminProjectUserAddDto 被添加项目成员信息
     */
    void adminAddProjectUser(AdminProjectUserAddDto adminProjectUserAddDto);

    /**
     * 项目经理删除项目信息
     *
     * @param id 项目ID
     */
    void adminDeleteProject(Long id);

    /**
     * 项目经理删除项目成员
     *
     * @param adminProjectUserDeleteDto 被删除项目成员信息
     */
    void adminDeleteProjectUser(AdminProjectUserDeleteDto adminProjectUserDeleteDto);

    /**
     * 项目经理修改项目信息
     *
     * @param adminProjectUpdateDto 被修改后的项目信息
     */
    void adminUpdateProject(AdminProjectUpdateDto adminProjectUpdateDto);

    /**
     * 管理员/项目经理导出项目信息
     *
     * @return 导出表格
     */
    List<AdminProjectExportVo> adminExportExcelList();
}
