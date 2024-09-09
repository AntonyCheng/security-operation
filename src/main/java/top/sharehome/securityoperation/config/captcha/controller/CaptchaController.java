package top.sharehome.securityoperation.config.captcha.controller;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sharehome.securityoperation.common.base.R;
import top.sharehome.securityoperation.config.captcha.condition.CaptchaCondition;
import top.sharehome.securityoperation.config.captcha.model.CaptchaCreate;
import top.sharehome.securityoperation.config.captcha.service.CaptchaService;

/**
 * 验证码控制器
 *
 * @author AntonyCheng
 */
@RestController
@Conditional(CaptchaCondition.class)
public class CaptchaController {

    @Resource
    private CaptchaService captchaService;

    /**
     * 获取验证码
     *
     * @return 返回注册结果
     */
    @PostMapping("/captcha")
    public R<CaptchaCreate> captcha() {
        return R.ok(captchaService.createCaptcha());
    }

}