package com.matzip.thread.user.domain;

import com.matzip.thread.role.domain.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.matzip.thread.role.domain.Role.ROLE_USER;

@Getter
@RequiredArgsConstructor
public class UserEntity {
    private final String username;
    private final String nickname;
    private final String password;
    private final Role role;

    public UserEntity changeRole(Role role) {
        return new UserEntity(
                username,
                nickname,
                password,
                role);
    }
}
