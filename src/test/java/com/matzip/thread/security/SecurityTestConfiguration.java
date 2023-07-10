package com.matzip.thread.security;

import com.matzip.thread.common.Repository.ResourcesRepository;
import com.matzip.thread.common.factorybean.PasswordEncoderFactoryBean;
import com.matzip.thread.security.configs.SecurityConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

@TestConfiguration
@Import(value = {SecurityConfig.class, PasswordEncoderFactoryBean.class})
public class SecurityTestConfiguration {

    private EntityManager em;

    @Bean
    public ResourcesRepository ResourcesRepository() {
        return new FakeResourcesRepository(em);
    }
}
