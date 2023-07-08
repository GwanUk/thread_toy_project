package com.matzip.thread;

import com.matzip.thread.security.token.ApiAuthenticationToken;
import com.matzip.thread.users.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class ApplicationConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof ApiAuthenticationToken) {
                return Optional.of(((User) authentication.getPrincipal()).getUsername());
            }

            return Optional.of("ANONYMOUS");
        };
    }
}
