package com.atguigu.guli.service.base.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.prefs.Preferences;

/**
 * @author zcgstart
 * @create 2020-02-12 15:20
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket AdminApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .apiInfo(AdminApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }

    private ApiInfo AdminApiInfo(){
        return new ApiInfoBuilder().title("后台管理系统API文档")
                .description("本文描述了谷粒学院后台管理系统课程中心的微服务接口")
                .version("2.5.6")
                .contact(new Contact("zcg","http://atguigu.com","1061076354@qq.com"))
                .build();
    }

    @Bean
    public Docket WebApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(WebApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build();
    }

    private ApiInfo WebApiInfo(){
        return new ApiInfoBuilder().title("谷粒学院网站API文档")
                .description("本文描述了谷粒学院网站课程中心的微服务接口")
                .version("2.5.6")
                .contact(new Contact("zcg","http://atguigu.com","1061076354@qq.com"))
                .build();
    }

}
