package top.sharehome.securityoperation.config.mail.condition;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 邮件自定义配置条件
 *
 * @author AntonyCheng
 */
public class MailCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String property = context.getEnvironment().getProperty("spring.mail.enable");
        return StringUtils.equals(Boolean.TRUE.toString(), property);
    }

}