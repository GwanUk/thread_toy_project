package com.matzip.thread.security.filter;

import com.matzip.thread.ipaddress.application.port.in.IpAddressSecurityPort;
import com.matzip.thread.role.application.prot.in.RoleSecurityPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.security.configs.SecurityConfig;
import com.matzip.thread.uri.application.port.in.UriSecurityPort;
import com.matzip.thread.uri.domain.UriEntity;
import com.matzip.thread.user.application.port.in.UserSecurityPort;
import com.matzip.thread.user.domain.PasswordEncoderFactoryBean;
import com.matzip.thread.user.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@TestConfiguration
@Import(value = {SecurityConfig.class,
        PasswordEncoderFactoryBean.class})
class SecurityTestConfiguration {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityTestController securityTestController() {
        return new SecurityTestController();
    }

    @Bean
    public UserSecurityPort userQueryInPort() {
        return username -> switch (username) {
            case "user" -> Optional.of(new UserEntity("user", "user", passwordEncoder.encode("1234"), Role.ROLE_USER));
            case "vip" -> Optional.of(new UserEntity("vip", "vip", passwordEncoder.encode("1234"), Role.ROLE_VIP));
            case "manager" -> Optional.of(new UserEntity("manager", "manager", passwordEncoder.encode("1234"), Role.ROLE_MANAGER));
            case "admin" -> Optional.of(new UserEntity("admin", "admin", passwordEncoder.encode("1234"), Role.ROLE_ADMIN));
            default -> Optional.empty();
        };
    }

    @Bean
    public UriSecurityPort uriInPort() {
        return () -> {
            UriEntity user = new UriEntity("/api/user", 1, List.of(Role.ROLE_USER));
            UriEntity vip = new UriEntity("/api/vip", 2, List.of(Role.ROLE_VIP));
            UriEntity manger = new UriEntity("/api/manger", 3, List.of(Role.ROLE_MANAGER));
            UriEntity admin = new UriEntity("/api/admin", 4, List.of(Role.ROLE_ADMIN));
            return List.of(user, vip, manger, admin);
        };
    }

    @Bean
    public RoleSecurityPort roleHierarchyInPort() {
        return () -> """
                ROLE_VIP > ROLE_USER
                ROLE_ADMIN > ROLE_VIP
                ROLE_ADMIN > ROLE_MANAGER
                """;
    }

    @Bean
    public IpAddressSecurityPort ipAddressQueryInPort() {
        return () -> List.of("0:0:0:0:0:0:0:1");
    }

    @ResponseBody
    @RequestMapping("/api")
    static class SecurityTestController {

        @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
        String user() {
            return "user";
        }

        @GetMapping(value = "/vip", produces = MediaType.APPLICATION_JSON_VALUE)
        String vip() {
            return "vip";
        }

        @GetMapping(value = "/manager", produces = MediaType.APPLICATION_JSON_VALUE)
        String manager() {
            return "manager";
        }

        @GetMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
        String admin() {
            return "admin";
        }
    }
}
