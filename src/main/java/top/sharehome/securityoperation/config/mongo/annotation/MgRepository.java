package top.sharehome.securityoperation.config.mongo.annotation;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Repository;
import top.sharehome.securityoperation.config.mongo.condition.MongoCondition;

import java.lang.annotation.*;

/**
 * MongoBD Repository注解
 *
 * @author AntonyCheng
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repository
@Conditional(MongoCondition.class)
public @interface MgRepository {
}
