package com.matzip.thread.security.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.thread.ipaddress.application.port.in.IpAddressQueryInPort;
import com.matzip.thread.role.application.event.RoleChangedEventHandler;
import com.matzip.thread.role.application.prot.in.RoleHierarchyPort;
import com.matzip.thread.security.filter.ApiAuthenticationProcessingFilter;
import com.matzip.thread.security.handler.ApiAccessDeniedHandler;
import com.matzip.thread.security.handler.ApiAuthenticationEntryPoint;
import com.matzip.thread.security.handler.ApiAuthenticationFailureHandler;
import com.matzip.thread.security.handler.ApiAuthenticationSuccessHandler;
import com.matzip.thread.security.service.ApiAuthenticationProvider;
import com.matzip.thread.security.service.IpAddressVoter;
import com.matzip.thread.security.service.UrlFilterInvocationSecurityMetadataSource;
import com.matzip.thread.security.service.UserDetailsServiceImpl;
import com.matzip.thread.uri.application.port.in.UriAllPort;
import com.matzip.thread.user.application.port.in.UserSecurityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
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
    private final UserSecurityPort userSecurityPort;
    private final UriAllPort uriAllPort;
    private final RoleHierarchyPort roleHierarchyPort;
    private final IpAddressQueryInPort ipAddressQueryInPort;

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
        return new UserDetailsServiceImpl(userSecurityPort);
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
        return new UrlFilterInvocationSecurityMetadataSource(uriAllPort);
    }

    private AffirmativeBased affirmativeBased() {
        return new AffirmativeBased(getAccessDecisionVoters());
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
        return List.of(ipAddressVoter(),
                roleHierarchyVoter());
    }

    @Bean
    public AccessDecisionVoter<?> ipAddressVoter() {
        return new IpAddressVoter(ipAddressQueryInPort);
    }

    @Bean
    public AccessDecisionVoter<?> roleHierarchyVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(roleHierarchyPort.getHierarchy());
        return roleHierarchy;
    }

    @Bean
    public RoleChangedEventHandler roleChangedEventHandler() {
        return new RoleChangedEventHandler(roleHierarchy(), roleHierarchyPort);
    }



}
