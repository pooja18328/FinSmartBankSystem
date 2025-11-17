package com.finsmartbank.FinSmartBankSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()          // disable CSRF for HTML form
            .authorizeHttpRequests()
                .requestMatchers("/**").permitAll() // allow all URLs
                .and()
            .formLogin().disable();    // disable default Spring Security login page

        return http.build();
    }
}
