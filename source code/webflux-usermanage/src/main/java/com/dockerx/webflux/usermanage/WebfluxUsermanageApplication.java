package com.dockerx.webflux.usermanage;

import com.dockerx.webflux.usermanage.handlers.UserHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@SpringBootApplication(exclude = MongoReactiveAutoConfiguration.class)
public class WebfluxUsermanageApplication {

    @Bean
    CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
        corsConfig.setMaxAge(8000L);
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedHeader("DockerX-Allowed");

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }


    @Bean
    public RouterFunction<ServerResponse> routes(UserHandler userHandler) {
        return RouterFunctions.nest(RequestPredicates.path("/api/users"),
                RouterFunctions
                        .route(GET(""), userHandler::findAllUsers)
                        .andRoute(GET("/{id}"), userHandler::findUser)
                        .andRoute(POST("").and(contentType(APPLICATION_JSON)), userHandler::createUser)
                        .andRoute(PUT("/{id}").and(contentType(APPLICATION_JSON)), userHandler::editUser)
                        .andRoute(DELETE("/{id}"), userHandler::deleteUser)
        );
    }
    public static void main(String[] args) {
        SpringApplication.run(WebfluxUsermanageApplication.class, args);
    }
}
