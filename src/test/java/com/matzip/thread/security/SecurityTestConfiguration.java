package com.matzip.thread.security;

import com.matzip.thread.common.factorybean.PasswordEncoderFactoryBean;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import com.matzip.thread.security.configs.SecurityConfig;
import com.matzip.thread.uri.application.port.in.UriInPort;
import com.matzip.thread.uri.domain.UriEntity;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@TestConfiguration
@Import(value = {SecurityConfig.class, PasswordEncoderFactoryBean.class})
public class SecurityTestConfiguration {

    @Bean
    public UriInPort uriInPort() {
        return new UriInPort() {
            @Override
            public List<UriEntity> findAll() {
                UriEntity resource1 = new UriEntity("/api/thread", 1, List.of(Role.ROLE_USER));
                UriEntity resource2 = new UriEntity("/api/admin", 2, List.of(Role.ROLE_ADMIN));
                return List.of(resource1, resource2);
            }

            @Override
            public void save(UriEntity uriEntity) {

            }
        };
    }
}
