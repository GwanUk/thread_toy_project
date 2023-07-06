package com.matzip.thread.security.configs;

import com.matzip.thread.security.filter.ApiLoginProcessingFilter;
import com.matzip.thread.security.handler.ApiAuthenticationFailureHandler;
import com.matzip.thread.security.handler.ApiAuthenticationSuccessHandler;
import com.matzip.thread.security.provider.ApiAuthenticationProvider;
import com.matzip.thread.security.service.CustomUserDetailsService;
import com.matzip.thread.users.application.port.in.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    private final UserUseCase userUseCase;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/users").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(apiLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable();

        return http.build();
    }

    public ApiLoginProcessingFilter apiLoginProcessingFilter() throws Exception {
        ApiLoginProcessingFilter apiLoginProcessingFilter = new ApiLoginProcessingFilter();
        apiLoginProcessingFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
//        apiLoginProcessingFilter.setAuthenticationManager(authenticationManager());
        apiLoginProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        apiLoginProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return apiLoginProcessingFilter;
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
        return new ApiAuthenticationProvider(userDetailsService(), passwordEncoder());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userUseCase);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
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
