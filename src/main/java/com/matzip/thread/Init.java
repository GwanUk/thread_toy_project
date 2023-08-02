package com.matzip.thread;

import com.matzip.thread.ipaddress.application.port.in.IpAddressWebPort;
import com.matzip.thread.ipaddress.domain.IpAddressEntity;
import com.matzip.thread.role.application.prot.in.RoleWebPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import com.matzip.thread.uri.application.port.in.UriWebPort;
import com.matzip.thread.uri.domain.UriEntity;
import com.matzip.thread.user.application.port.in.UserWebPort;
import com.matzip.thread.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class Init {

    private final RoleWebPort roleWebPort;

    private final UriWebPort uriWebPort;

    private final UserWebPort userWebPort;

    private final IpAddressWebPort ipAddressWebPort;

    private final PasswordEncoder passwordEncoder;


//    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        RoleEntity roleUser = new RoleEntity(Role.ROLE_USER, "ROLE_USER", null);
        RoleEntity roleVip = new RoleEntity(Role.ROLE_VIP, "ROLE_VIP", List.of(roleUser));
        roleWebPort.save(roleVip);

        UriEntity uriUser = new UriEntity("/api/user/**", 1, List.of(Role.ROLE_USER));
        UriEntity uriVip = new UriEntity("/api/vip/**", 2, List.of(Role.ROLE_VIP));
        uriWebPort.save(uriUser);
        uriWebPort.save(uriVip);

        String pwd = passwordEncoder.encode("1234");
        UserEntity user = new UserEntity("user", "userMan", pwd, Role.ROLE_USER);
        UserEntity vip = new UserEntity("vip", "vipMan", pwd, Role.ROLE_VIP);
        userWebPort.save(user);
        userWebPort.save(vip);

        IpAddressEntity ipAddress = new IpAddressEntity("0:0:0:0:0:0:0:1");
        ipAddressWebPort.save(ipAddress);
    }
}
