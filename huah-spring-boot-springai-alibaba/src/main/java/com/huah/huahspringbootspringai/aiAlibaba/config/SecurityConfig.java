package com.huah.huahspringbootspringai.aiAlibaba.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 替代 configure(HttpSecurity)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/login/oauth2/code/**","/index.html", "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf().disable()
                .httpBasic().disable(); // 注意：需显式启用

        return http.build();
    }

    // 替代 configure(WebSecurity)
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/static/**", "/css/**");
    }

    private AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
            String targetUrl = savedRequest != null ? savedRequest.getRedirectUrl() : "/";
            response.sendRedirect(targetUrl);
        };
    }

    private LogoutSuccessHandler customLogoutSuccessHandler() {
        return (request, response, authentication) -> {
            SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
            String targetUrl = savedRequest != null ? savedRequest.getRedirectUrl() : "/";
            response.sendRedirect(targetUrl);
        };
    }
}
