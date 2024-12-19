package com.example.AuthServer.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (테스트용)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/kakao", "/api/auth/logout", "/api/auth/kakao/callback", "/api/auth/success", "/api/auth/session").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/api/auth/kakao")
                        .defaultSuccessUrl("/api/auth/success")
                        .failureUrl("/api/auth/failure")
                );

        return http.build();
    }
}