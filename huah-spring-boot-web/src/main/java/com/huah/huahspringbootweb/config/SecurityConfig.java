package com.huah.huahspringbootweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/login/oauth2/code/**").permitAll() // 开放根路径和登录端点
                .anyRequest().authenticated() // 其他所有端点需要认证
                .and()
                .oauth2Login()
//                .successHandler(customAuthenticationSuccessHandler())
                .and()
                .logout()
                .logoutSuccessHandler(customLogoutSuccessHandler());
        http.sessionManagement()
                .sessionFixation().newSession() // 修复会话固定
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
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
