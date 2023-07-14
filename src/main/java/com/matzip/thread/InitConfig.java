package com.matzip.thread;

import com.matzip.thread.role.application.prot.in.RoleInPort;
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
public class InitConfig {

    private final RoleInPort roleInPort;
    private final UriInPort uriInPort;
    private final UserInPort userInPort;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        roleInPort.save(new RoleEntity(Role.ROLE_ADMIN, "관리자 권한", null, List.of()));
        roleInPort.save(new RoleEntity(Role.ROLE_VIP, "특급 권한", Role.ROLE_ADMIN, List.of()));

        uriInPort.save(new UriEntity("/api/admin/**", 1, List.of(Role.ROLE_ADMIN)));
        uriInPort.save(new UriEntity("/api/vip/**", 1, List.of(Role.ROLE_VIP)));

        userInPort.signUp(new UserEntity("user", "kim", "1234", null), Role.ROLE_VIP);
    }
}
