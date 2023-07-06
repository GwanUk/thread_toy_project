package com.matzip.thread.users.security.configs;

import com.matzip.thread.users.application.port.in.UserUseCase;
import com.matzip.thread.users.security.filter.ApiLoginProcessingFilter;
import com.matzip.thread.users.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authConfiguration;

    private final UserUseCase userUseCase;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/users").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(apiLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        http.userDetailsService(userDetailsService());

        http.csrf().disable();

        return http.build();
    }

    public ApiLoginProcessingFilter apiLoginProcessingFilter() {
        ApiLoginProcessingFilter apiLoginProcessingFilter = new ApiLoginProcessingFilter();
        apiLoginProcessingFilter.setAuthenticationManager(authenticationManager());
        return apiLoginProcessingFilter;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userUseCase);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        try {
            return authConfiguration.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
