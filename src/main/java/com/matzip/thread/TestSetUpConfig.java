package com.matzip.thread;

import com.matzip.thread.ipaddress.application.port.in.IpAddressInPort;
import com.matzip.thread.role.application.prot.in.RoleWebPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import com.matzip.thread.uri.application.port.in.UriInPort;
import com.matzip.thread.uri.domain.UriEntity;
import com.matzip.thread.user.application.port.in.UserInPort;
import com.matzip.thread.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class TestSetUpConfig {

    private final RoleWebPort roleWebPort;
    private final UriInPort uriInPort;
    private final UserInPort userInPort;
    private final IpAddressInPort ipAddressInPort;

    @EventListener(ApplicationReadyEvent.class)
    public void setUp() {
        roleWebPort.save(new RoleEntity(Role.ROLE_ADMIN, "관리자 권한", null, List.of()));
        roleWebPort.save(new RoleEntity(Role.ROLE_VIP, "특급 권한", Role.ROLE_ADMIN, List.of()));

        uriInPort.save(new UriEntity("/api/admin/**", 1, List.of(Role.ROLE_ADMIN)));
        uriInPort.save(new UriEntity("/api/vip/**", 1, List.of(Role.ROLE_VIP)));

        userInPort.signUp(new UserEntity("user", "kim", "1234", null), Role.ROLE_VIP);

        ipAddressInPort.save("127.0.0.1");
        ipAddressInPort.save("0:0:0:0:0:0:0:1");
    }
}
