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
import top.sharehome.securityoperation.mapper.UserMapper;
import top.sharehome.securityoperation.model.dto.project.AdminProjectAddDto;
import top.sharehome.securityoperation.model.dto.project.AdminProjectPageDto;
import top.sharehome.securityoperation.model.dto.projectUser.AdminProjectUserAddDto;
import top.sharehome.securityoperation.model.entity.Project;
import top.sharehome.securityoperation.model.entity.ProjectUser;
import top.sharehome.securityoperation.model.entity.User;
import top.sharehome.securityoperation.model.page.PageModel;
import top.sharehome.securityoperation.model.vo.auth.AuthLoginVo;
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
            Map<String, String> userInfo = projectUserMapper.selectList(projectUserLambdaQueryWrapper).stream().map(projectUser -> userMapper.selectById(projectUser.getUserId())).collect(Collectors.toMap(User::getWorkId, User::getName));
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
        if (Objects.isNull(projectMapper.selectById(projectId))){
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

}




