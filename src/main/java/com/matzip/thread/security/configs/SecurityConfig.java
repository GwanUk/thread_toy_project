package com.matzip.thread.security.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.thread.security.filter.ApiAuthenticationProcessingFilter;
import com.matzip.thread.security.handler.ApiAccessDeniedHandler;
import com.matzip.thread.security.handler.ApiAuthenticationEntryPoint;
import com.matzip.thread.security.handler.ApiAuthenticationFailureHandler;
import com.matzip.thread.security.handler.ApiAuthenticationSuccessHandler;
import com.matzip.thread.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import com.matzip.thread.security.provider.ApiAuthenticationProvider;
import com.matzip.thread.security.service.UserDetailsServiceImpl;
import com.matzip.thread.uri.application.port.in.UriInPort;
import com.matzip.thread.user.application.port.in.UserInPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ObjectMapper objectMapper;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final PasswordEncoder passwordEncoder;
    private final UserInPort userInPort;

    private final UriInPort uriInPort;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .authorizeRequests()
                .anyRequest()
                .permitAll();

        http.csrf().disable();

        http.addFilterBefore(apiLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(ApiFilterSecurityInterceptor(), FilterSecurityInterceptor.class);

        http.exceptionHandling()
                .authenticationEntryPoint(new ApiAuthenticationEntryPoint())
                .accessDeniedHandler(new ApiAccessDeniedHandler());

        return http.build();
    }

    public ApiAuthenticationProcessingFilter apiLoginProcessingFilter() throws Exception {
        ApiAuthenticationProcessingFilter apiAuthenticationProcessingFilter = new ApiAuthenticationProcessingFilter(objectMapper);
        apiAuthenticationProcessingFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
        apiAuthenticationProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        apiAuthenticationProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return apiAuthenticationProcessingFilter;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new ApiAuthenticationProvider(userDetailsService(), passwordEncoder);
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userInPort);
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new ApiAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new ApiAuthenticationFailureHandler();
    }

    public FilterSecurityInterceptor ApiFilterSecurityInterceptor() throws Exception {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
        filterSecurityInterceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
        return filterSecurityInterceptor;
    }

    @Bean
    public UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() {
        return new UrlFilterInvocationSecurityMetadataSource(uriInPort);
    }

    private AffirmativeBased affirmativeBased() {
        return new AffirmativeBased(getAccessDecisionVoters());
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
//        return List.of(accessDecisionVoter());
        return List.of(new RoleVoter());
    }

//    @Bean
//    public AccessDecisionVoter<?> accessDecisionVoter() {
//        return new RoleHierarchyVoter(roleHierarchy());
//    }
//
//    @Bean
//    public RoleHierarchyImpl roleHierarchy() {
//        return new RoleHierarchyImpl();
//    }
}
