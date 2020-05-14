package com.hsnam.rest.docker.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.PostConstruct;

/**
 * com.hsnam.rest.docker.api.config
 * Swagger Configuration
 * @author hsnam
 */
@Slf4j
@Configuration
public class SwaggerConfig {
    /**
     * Swagger Setting
     * @return
     */
    @Bean
    public Docket swaggerAPI(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerApiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.hsnam.rest.docker.api"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }//end of swaggerAPI

    /**
     * swagger API Information
     * @return
     */
    private ApiInfo swaggerApiInfo(){
        return new ApiInfoBuilder().title("Docker API")
                .description("Project - Docker 컨테이너 관리 API")
                .license("hsnam").version("1.0.0").build();
    }//end of swaggerApiInfo

    @PostConstruct
    public void init(){
        log.info("Swagger Configuration.");
    }
}
