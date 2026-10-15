package com.lawlayui.e_commerce.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.lawlayui.e_commerce.infrastructure.services.CustomOAuth2UserService;

@Configuration 
public class SecurityConfig {
    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        http
            .csrf(csrf -> csrf.disable())   
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login/**").permitAll()
                .anyRequest().authenticated()
            )

            .oauth2Login(oauth2 -> oauth2 
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                )
            );
        return http.build();
    }
}
