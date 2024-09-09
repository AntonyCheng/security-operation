package top.sharehome.securityoperation.config.captcha.service;

import top.sharehome.securityoperation.config.captcha.model.CaptchaCreate;

/**
 * 验证码服务接口
 *
 * @author AntonyCheng
 */
public interface CaptchaService {

    /**
     * 生成验证码
     */
    CaptchaCreate createCaptcha();

    /**
     * 校验验证码
     *
     * @param code 验证码
     * @param uuid 验证码的UUID
     */
    void checkCaptcha(String code, String uuid);

}