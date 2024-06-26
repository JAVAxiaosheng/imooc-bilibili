package com.imooc.bilibili.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.imooc.bilibili.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo()).groupName("imooc.bilibili");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Imooc Bilibili Rest Api")
                .description("Some custom description of Api.")
                .build();
    }
}
