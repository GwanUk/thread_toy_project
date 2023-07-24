package com.matzip.thread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class ApplicationConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());

    }
}
