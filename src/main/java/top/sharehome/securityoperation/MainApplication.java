package top.sharehome.securityoperation;

import org.dromara.easyes.starter.register.EsMapperScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动类
 *
 * @author AntonyCheng
 */
@SpringBootApplication(scanBasePackages = {"top.sharehome.securityoperation.**"})
@MapperScan(basePackages = {"top.sharehome.securityoperation.mapper"})
@EsMapperScan(value = "top.sharehome.securityoperation.elasticsearch.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = {"top.sharehome.securityoperation.config.**"})
@EnableTransactionManagement
@ServletComponentScan(basePackages = {"top.sharehome.securityoperation.config.**"})
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}