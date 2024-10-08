package top.sharehome.securityoperation.config.job.schedule.condition;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * SpringBoot Schedule注解循环任务自定义配置条件
 *
 * @author AntonyCheng
 */
public class ScheduleCycleCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String property = context.getEnvironment().getProperty("schedule.cycle.enable");
        return StringUtils.equals(Boolean.TRUE.toString(), property);
    }

}