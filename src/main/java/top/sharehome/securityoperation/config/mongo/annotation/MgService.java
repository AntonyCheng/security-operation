package top.sharehome.securityoperation.config.mongo.annotation;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import top.sharehome.securityoperation.config.mongo.condition.MongoCondition;

import java.lang.annotation.*;

/**
 * MongoBD Service注解
 *
 * @author AntonyCheng
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
@Conditional(MongoCondition.class)
public @interface MgService {
}
