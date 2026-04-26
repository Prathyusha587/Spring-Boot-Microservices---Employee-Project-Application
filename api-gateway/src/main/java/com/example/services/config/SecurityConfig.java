package com.example.services.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeExchange(auth -> auth

                // ✅ Auth service
                .pathMatchers("/auth/**").permitAll()

                // ✅ Employee service (FIXED)
                .pathMatchers("/employees/**").permitAll()

                // ✅ Project service (FIXED)
                .pathMatchers("/api/project/**").permitAll()

                // ✅ CORS preflight
                .pathMatchers(HttpMethod.OPTIONS).permitAll()

                // ⚠️ Keep this for now (safe mode)
                .anyExchange().permitAll()
            )
            .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }

    // ✅ CORS configuration for frontend
    @Bean
    public CorsWebFilter corsWebFilter() {

        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://127.0.0.1:5500");
        config.addAllowedOrigin("http://localhost:5500");

        // safer option (you can switch later)
        // config.addAllowedOriginPattern("http://127.0.0.1:*");
        // config.addAllowedOriginPattern("http://localhost:*");

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.addAllowedHeader("*");

        // needed for JWT later
        config.addExposedHeader("Authorization");

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}