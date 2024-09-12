package top.sharehome.securityoperation.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jodd.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sharehome.securityoperation.common.base.Constants;
import top.sharehome.securityoperation.common.base.ReturnCode;
import top.sharehome.securityoperation.exception.customize.CustomizeReturnException;
import top.sharehome.securityoperation.mapper.ProjectMapper;
import top.sharehome.securityoperation.mapper.ProjectUserMapper;
import top.sharehome.securityoperation.mapper.TaskMapper;
import top.sharehome.securityoperation.mapper.UserMapper;
import top.sharehome.securityoperation.model.dto.project.AdminProjectAddDto;
import top.sharehome.securityoperation.model.dto.project.AdminProjectPageDto;
import top.sharehome.securityoperation.model.dto.project.AdminProjectUpdateDto;
import top.sharehome.securityoperation.model.dto.projectUser.AdminProjectUserAddDto;
import top.sharehome.securityoperation.model.dto.projectUser.AdminProjectUserDeleteDto;
import top.sharehome.securityoperation.model.entity.Project;
import top.sharehome.securityoperation.model.entity.ProjectUser;
import top.sharehome.securityoperation.model.entity.Task;
import top.sharehome.securityoperation.model.entity.User;
import top.sharehome.securityoperation.model.page.PageModel;
import top.sharehome.securityoperation.model.vo.auth.AuthLoginVo;
import top.sharehome.securityoperation.model.vo.project.AdminProjectExportVo;
import top.sharehome.securityoperation.model.vo.project.AdminProjectPageVo;
import top.sharehome.securityoperation.service.ProjectService;
import top.sharehome.securityoperation.utils.satoken.LoginUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 项目服务实现类
 *
 * @author AntonyCheng
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ProjectUserMapper projectUserMapper;

    @Resource
    private TaskMapper taskMapper;

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Page<AdminProjectPageVo> adminPageProject(AdminProjectPageDto adminProjectPageDto, PageModel pageModel) {
        Page<Project> page = pageModel.build();
        Page<AdminProjectPageVo> res = pageModel.build();

        // 关联子表查询结果
        List<Long> maybeProjectIds = new ArrayList<>();
        if (Objects.nonNull(adminProjectPageDto.getUserId())) {
            LambdaQueryWrapper<ProjectUser> projectUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
            projectUserLambdaQueryWrapper
                    .eq(ProjectUser::getUserId, adminProjectPageDto.getUserId());
            maybeProjectIds = projectUserMapper.selectList(projectUserLambdaQueryWrapper).stream().map(ProjectUser::getProjectId).toList();
            if (maybeProjectIds.isEmpty()) {
                return res;
            }
        }

        LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 构造查询条件
        projectLambdaQueryWrapper
                .like(StringUtil.isNotBlank(adminProjectPageDto.getName()), Project::getName, adminProjectPageDto.getName())
                .like(StringUtil.isNotBlank(adminProjectPageDto.getDescription()), Project::getDescription, adminProjectPageDto.getDescription())
                .like(StringUtil.isNotBlank(adminProjectPageDto.getUrl()), Project::getUrl, adminProjectPageDto.getUrl())
                .in(!maybeProjectIds.isEmpty(), Project::getId, maybeProjectIds);

        // 如果是项目经理，那就仅能查询其创建的项目
        AuthLoginVo loginUser = LoginUtils.getLoginUser();
        if (StringUtils.equals(loginUser.getRole(), Constants.ROLE_MANAGER)) {
            projectLambdaQueryWrapper.eq(Project::getManagerId, loginUser.getId());
        } else {
            projectLambdaQueryWrapper.eq(Objects.nonNull(adminProjectPageDto.getManagerId()), Project::getManagerId, adminProjectPageDto.getManagerId());
        }

        // 构造查询排序
        projectLambdaQueryWrapper.orderByAsc(Project::getCreateTime);

        // 分页查询
        projectMapper.selectPage(page, projectLambdaQueryWrapper);

        // 返回值处理（Entity ==> Vo）
        List<AdminProjectPageVo> newRecords = page.getRecords().stream().map(project -> {
            User userInDatabase = userMapper.selectById(project.getManagerId());
            LambdaQueryWrapper<ProjectUser> projectUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
            projectUserLambdaQueryWrapper
                    .eq(ProjectUser::getProjectId, project.getId())
                    .orderByAsc(ProjectUser::getCreateTime);
            Map<String, String> userInfo = projectUserMapper.selectList(projectUserLambdaQueryWrapper).stream().map(projectUser -> userMapper.selectById(projectUser.getUserId())).collect(Collectors.toMap(user -> Long.toString(user.getId()), user -> user.getName() + " | " + user.getWorkId()));
            userInfo.remove(Long.toString(loginUser.getId()));
            return new AdminProjectPageVo()
                    .setId(project.getId())
                    .setName(project.getName())
                    .setDescription(project.getDescription())
                    .setUrl(project.getUrl())
                    .setManagerName(userInDatabase.getName())
                    .setUserInfoJson(JSON.toJSONString(userInfo))
                    .setCreateTime(userInDatabase.getCreateTime());
        }).toList();
        BeanUtils.copyProperties(page, res, "records");
        res.setRecords(newRecords);

        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adminAddProject(AdminProjectAddDto adminProjectAddDto) {
        Project project = new Project()
                .setName(adminProjectAddDto.getName())
                .setDescription(adminProjectAddDto.getDescription())
                .setUrl(adminProjectAddDto.getUrl())
                .setManagerId(LoginUtils.getLoginUserId());
        if (!this.save(project)) {
            throw new CustomizeReturnException(ReturnCode.ERRORS_OCCURRED_IN_THE_DATABASE_SERVICE);
        }
        int insertResult = projectUserMapper.insert(
                new ProjectUser()
                        .setProjectId(project.getId())
                        .setUserId(LoginUtils.getLoginUserId())
                        .setUserRole(Constants.ROLE_MANAGER)
        );
        if (insertResult == 0) {
            throw new CustomizeReturnException(ReturnCode.ERRORS_OCCURRED_IN_THE_DATABASE_SERVICE);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adminAddProjectUser(AdminProjectUserAddDto adminProjectUserAddDto) {
        Long projectId = adminProjectUserAddDto.getProjectId();
        if (Objects.isNull(projectMapper.selectById(projectId))) {
            throw new CustomizeReturnException(ReturnCode.FAIL, "项目[" + projectId + "]不存在");
        }
        LambdaQueryWrapper<ProjectUser> projectUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        projectUserLambdaQueryWrapper.eq(ProjectUser::getProjectId, projectId);
        List<Long> idsInDatabase = projectUserMapper.selectList(projectUserLambdaQueryWrapper).stream().map(ProjectUser::getUserId).toList();
        List<Long> idsInParams = adminProjectUserAddDto.getUserIds().stream().distinct().toList();
        idsInParams.forEach(id -> {
            if (Objects.isNull(userMapper.selectById(id))) {
                throw new CustomizeReturnException(ReturnCode.USER_ACCOUNT_DOES_NOT_EXIST, "用户[" + id + "]不存在");
            }
        });
        if (CollectionUtils.containsAny(idsInDatabase, idsInParams)) {
            throw new CustomizeReturnException(ReturnCode.USERNAME_ALREADY_EXISTS, "多次添加同一个项目成员");
        }
        List<ProjectUser> projectUserList = idsInParams.stream().map(userId -> new ProjectUser()
                .setProjectId(projectId)
                .setUserId(userId)
                .setUserRole(Constants.ROLE_USER)).toList();
        projectUserMapper.insert(projectUserList, 200);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adminDeleteProject(Long id) {
        Project projectInDatabase = projectMapper.selectById(id);
        if (Objects.isNull(projectInDatabase)) {
            throw new CustomizeReturnException(ReturnCode.FAIL, "项目不存在");
        }
        if (!Objects.equals(LoginUtils.getLoginUserId(), projectInDatabase.getManagerId())) {
            throw new CustomizeReturnException(ReturnCode.ABNORMAL_USER_OPERATION, "无法对不属于自己的项目进行操作");
        }
        // 删除项目信息
        int projectDeleteResult = projectMapper.deleteById(id);
        if (projectDeleteResult == 0) {
            throw new CustomizeReturnException(ReturnCode.ERRORS_OCCURRED_IN_THE_DATABASE_SERVICE);
        }
        // 删除项目用户关联关系
        LambdaQueryWrapper<ProjectUser> projectUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        projectUserLambdaQueryWrapper.eq(ProjectUser::getProjectId, projectInDatabase.getId());
        int userDeleteResult = projectUserMapper.delete(projectUserLambdaQueryWrapper);
        if (userDeleteResult == 0) {
            throw new CustomizeReturnException(ReturnCode.ERRORS_OCCURRED_IN_THE_DATABASE_SERVICE);
        }
        // 删除项目下属任务
        LambdaQueryWrapper<Task> taskLambdaQueryWrapper = new LambdaQueryWrapper<>();
        taskLambdaQueryWrapper.eq(Task::getProjectId, projectInDatabase.getId());
        taskMapper.delete(taskLambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adminDeleteProjectUser(AdminProjectUserDeleteDto adminProjectUserDeleteDto) {
        Project projectInDatabase = projectMapper.selectById(adminProjectUserDeleteDto.getProjectId());
        if (Objects.isNull(projectInDatabase)) {
            throw new CustomizeReturnException(ReturnCode.FAIL, "项目不存在");
        }
        if (!Objects.equals(LoginUtils.getLoginUserId(), projectInDatabase.getManagerId())) {
            throw new CustomizeReturnException(ReturnCode.ABNORMAL_USER_OPERATION, "无法对不属于自己的项目进行操作");
        }
        LambdaQueryWrapper<ProjectUser> projectUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        projectUserLambdaQueryWrapper
                .eq(ProjectUser::getProjectId, adminProjectUserDeleteDto.getProjectId())
                .eq(ProjectUser::getUserId, adminProjectUserDeleteDto.getUserId());
        if (projectUserMapper.exists(projectUserLambdaQueryWrapper)) {
            projectUserMapper.delete(projectUserLambdaQueryWrapper);
        } else {
            throw new CustomizeReturnException(ReturnCode.FAIL, "该用户不是该项目的成员");
        }
        // 删除该项目分配给该用户的任务
        LambdaQueryWrapper<Task> taskLambdaQueryWrapper = new LambdaQueryWrapper<>();
        taskLambdaQueryWrapper
                .eq(Task::getProjectId, adminProjectUserDeleteDto.getProjectId())
                .eq(Task::getUserId, adminProjectUserDeleteDto.getUserId());
        taskMapper.delete(taskLambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdateProject(AdminProjectUpdateDto adminProjectUpdateDto) {
        Project projectInDatabase = projectMapper.selectById(adminProjectUpdateDto.getId());
        if (Objects.isNull(projectInDatabase)) {
            throw new CustomizeReturnException(ReturnCode.FAIL, "项目不存在");
        }
        if (!Objects.equals(LoginUtils.getLoginUserId(), projectInDatabase.getManagerId())) {
            throw new CustomizeReturnException(ReturnCode.ABNORMAL_USER_OPERATION, "无法对不属于自己的项目进行操作");
        }
        // 数据只有发生更新之后才可以进行数据库操作
        if (Objects.equals(adminProjectUpdateDto.getName(), projectInDatabase.getName()) &&
                Objects.equals(adminProjectUpdateDto.getDescription(), projectInDatabase.getDescription()) &&
                Objects.equals(adminProjectUpdateDto.getUrl(), projectInDatabase.getUrl())) {
            throw new CustomizeReturnException(ReturnCode.FAIL, "信息未发生更改");
        }
        Project project = new Project()
                .setId(adminProjectUpdateDto.getId())
                .setName(adminProjectUpdateDto.getName())
                .setDescription(adminProjectUpdateDto.getDescription())
                .setUrl(adminProjectUpdateDto.getUrl());
        int updateResult = projectMapper.updateById(project);
        if (updateResult == 0) {
            throw new CustomizeReturnException(ReturnCode.ERRORS_OCCURRED_IN_THE_DATABASE_SERVICE);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AdminProjectExportVo> adminExportExcelList() {
        List<Project> projectInDatabase;
        if (LoginUtils.isAdmin()) {
            projectInDatabase = projectMapper.selectList(null);
        } else {
            LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            projectLambdaQueryWrapper
                    .eq(Project::getManagerId, LoginUtils.getLoginUserId());
            projectInDatabase = projectMapper.selectList(projectLambdaQueryWrapper);
        }
        return projectInDatabase.stream().map(project -> {
            AdminProjectExportVo adminProjectExportVo = new AdminProjectExportVo();
            adminProjectExportVo.setId(project.getId());
            adminProjectExportVo.setName(project.getName());
            adminProjectExportVo.setDescription(project.getDescription());
            adminProjectExportVo.setUrl(project.getUrl());
            User userInDatabase = userMapper.selectById(project.getManagerId());
            adminProjectExportVo.setManagerName(userInDatabase.getName());
            LambdaQueryWrapper<ProjectUser> projectUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
            projectUserLambdaQueryWrapper.eq(ProjectUser::getProjectId, project.getId());
            List<Long> userIds = projectUserMapper.selectList(projectUserLambdaQueryWrapper).stream().map(ProjectUser::getUserId).toList();
            String userInfo = userMapper.selectBatchIds(userIds).stream().map(user -> "[" + user.getWorkId() + "-" + user.getName() + "-" + user.getAccount() + "]").collect(Collectors.joining("，"));
            adminProjectExportVo.setUserInfo(userInfo);
            adminProjectExportVo.setCreateTime(project.getCreateTime());
            adminProjectExportVo.setUpdateTime(project.getUpdateTime());
            return adminProjectExportVo;
        }).toList();
    }

}




