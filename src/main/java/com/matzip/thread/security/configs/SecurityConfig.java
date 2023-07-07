package com.matzip.thread.security.configs;

import com.matzip.thread.security.entrypoint.ApiAuthenticationEntryPoint;
import com.matzip.thread.security.filter.ApiAuthenticationProcessingFilter;
import com.matzip.thread.security.handler.ApiAccessDeniedHandler;
import com.matzip.thread.security.handler.ApiAuthenticationFailureHandler;
import com.matzip.thread.security.handler.ApiAuthenticationSuccessHandler;
import com.matzip.thread.security.provider.ApiAuthenticationProvider;
import com.matzip.thread.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {

        http.antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/admin").hasRole("ADMIN")
                .antMatchers("/api/users").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(apiLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling()
                .authenticationEntryPoint(new ApiAuthenticationEntryPoint())
                .accessDeniedHandler(new ApiAccessDeniedHandler());

        http.csrf().disable();

        return http.build();
    }

    public ApiAuthenticationProcessingFilter apiLoginProcessingFilter() throws Exception {
        ApiAuthenticationProcessingFilter apiAuthenticationProcessingFilter = new ApiAuthenticationProcessingFilter();
        apiAuthenticationProcessingFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
//        apiLoginProcessingFilter.setAuthenticationManager(authenticationManager());
        apiAuthenticationProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        apiAuthenticationProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return apiAuthenticationProcessingFilter;
    }

//    @Bean
//    public AuthenticationManager authenticationManager() {
//        ProviderManager authenticationManager = null;
//        try {
//            authenticationManager = (ProviderManager) authenticationConfiguration.getAuthenticationManager();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        authenticationManager.getProviders().add(authenticationProvider());
//        return authenticationManager;
//    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new ApiAuthenticationProvider();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new ApiAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new ApiAuthenticationFailureHandler();
    }
}
