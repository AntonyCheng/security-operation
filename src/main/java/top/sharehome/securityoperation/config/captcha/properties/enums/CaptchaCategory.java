package top.sharehome.securityoperation.config.captcha.properties.enums;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码类别
 *
 * @author AntonyCheng
 */
@Getter
@AllArgsConstructor
public enum CaptchaCategory {

    /**
     * 线段干扰
     */
    LINE(LineCaptcha.class),

    /**
     * 圆圈干扰
     */
    CIRCLE(CircleCaptcha.class),

    /**
     * 扭曲干扰
     */
    SHEAR(ShearCaptcha.class);

    private final Class<? extends AbstractCaptcha> clazz;

}