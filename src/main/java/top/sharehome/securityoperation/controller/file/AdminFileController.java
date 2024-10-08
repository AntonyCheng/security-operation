package top.sharehome.securityoperation.controller.file;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.sharehome.securityoperation.common.base.Constants;
import top.sharehome.securityoperation.common.base.R;
import top.sharehome.securityoperation.config.log.annotation.ControllerLog;
import top.sharehome.securityoperation.config.log.enums.Operator;
import top.sharehome.securityoperation.model.dto.file.AdminFilePageDto;
import top.sharehome.securityoperation.model.page.PageModel;
import top.sharehome.securityoperation.model.vo.file.AdminFileExportVo;
import top.sharehome.securityoperation.model.vo.file.AdminFilePageVo;
import top.sharehome.securityoperation.service.FileService;
import top.sharehome.securityoperation.utils.document.excel.ExcelUtils;

import java.util.List;

/**
 * 管理员管理文件控制器
 *
 * @author AntonyCheng
 */
@RestController
@RequestMapping("/admin/file")
@SaCheckLogin
@SaCheckRole(value = {Constants.ROLE_ADMIN})
public class AdminFileController {

    @Resource
    private FileService fileService;

    /**
     * 管理员分页查询文件信息
     *
     * @param adminFilePageDto 文件信息查询条件
     * @param pageModel        分页模型
     * @return 分页查询结果
     */
    @GetMapping("/page")
    @ControllerLog(description = "管理员查询文件信息", operator = Operator.QUERY)
    public R<Page<AdminFilePageVo>> pageFile(AdminFilePageDto adminFilePageDto, PageModel pageModel) {
        // 处理一下文件后缀名查询条件，如果存在则转为小写
        if (StringUtils.isNotBlank(adminFilePageDto.getOssType())) {
            adminFilePageDto.setOssType(adminFilePageDto.getOssType().toLowerCase());
        }
        Page<AdminFilePageVo> file = fileService.adminPageFile(adminFilePageDto, pageModel);
        return R.ok(file);
    }

    /**
     * 管理员删除文件信息
     *
     * @param id 文件ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    @ControllerLog(description = "管理员删除文件信息", operator = Operator.DELETE)
    public R<String> deleteFile(@PathVariable("id") Long id) {
        fileService.adminDeleteFile(id);
        return R.ok("删除成功");
    }

    /**
     * 导出文件表格
     *
     * @return 导出表格
     */
    @GetMapping("/export")
    @ControllerLog(description = "管理员导出文件表格", operator = Operator.QUERY)
    public R<Void> exportFile(HttpServletResponse response) {
        List<AdminFileExportVo> list = fileService.adminExportExcelList();
        ExcelUtils.exportHttpServletResponse(list, "文件表", AdminFileExportVo.class, response);
        return R.empty();
    }

}
