package com.dockerx.webflux.usermanage.configuration;

import com.dockerx.webflux.usermanage.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author Author  知秋
 * @email fei6751803@163.com
 * @time Created by Auser on 2018/8/9 21:35.
 */
@Configuration("com.dockerx.webflux.usermanage")
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.csrf().disable()
                   .authorizeExchange()
                   .pathMatchers(HttpMethod.GET, "/api/users").authenticated()
                   .pathMatchers(HttpMethod.POST, "/api/users/**").authenticated()
                   .pathMatchers(HttpMethod.PUT, "/api/users/**").authenticated()
                   .pathMatchers(HttpMethod.DELETE, "/api/users/**").hasAuthority("ADMIN")
                   .anyExchange().permitAll()
                   .and()
                   .httpBasic()
                   .and()
                   .build();
    }


    @Bean
    public ReactiveUserDetailsService userDetailsRepository(UserRepository users) {
        return (username) ->
                users.findOneByUsername(username).cast(UserDetails.class);
    }

}
