package com.matzip.thread.security;

import com.matzip.thread.common.factorybean.PasswordEncoderFactoryBean;
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
                RoleEntity role1 = new RoleEntity("ROLE_USER", "유저 권한");
                UriEntity resource1 = new UriEntity("/api/thread", 1, List.of(role1));

                RoleEntity role2 = new RoleEntity("ROLE_ADMIN", "관리자 권한");
                UriEntity resource2 = new UriEntity("/api/admin", 2, List.of(role2));
                return List.of(resource1, resource2);
            }
        };
    }
}
