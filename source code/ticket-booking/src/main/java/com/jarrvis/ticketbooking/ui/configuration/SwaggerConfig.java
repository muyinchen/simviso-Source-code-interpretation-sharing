package com.jarrvis.ticketbooking.ui.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static java.util.Collections.singletonList;

/**
 * Main configuration class to enable the Swagger UI frontend. It registers all supported controllers and describes the
 * API.
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jarrvis.ticketbooking.ui.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .securityContexts(singletonList(actuatorSecurityContext()))
                .securitySchemes(singletonList(basicAuthScheme()))
                .apiInfo(apiInfo());
    }

    private SecurityScheme basicAuthScheme() {
        return new BasicAuth("basicAuth");
    }

    private SecurityReference basicAuthReference() {
        return new SecurityReference("basicAuth", new AuthorizationScope[0]);
    }

    private SecurityContext actuatorSecurityContext() {
        return SecurityContext.builder()
                .securityReferences(singletonList(basicAuthReference()))
                .forPaths(PathSelectors.ant("/movies/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "ticket booking REST API",
                "Multiplex ticket booking.",
                "1.0.0-dev",
"",
                new Contact("Michal Kaliszewski", "https://github.com/jarrvis", "michal.kaliszewski00@gmail.com"),
                "License of API", "API license URL", Collections.emptyList());
    }
}
