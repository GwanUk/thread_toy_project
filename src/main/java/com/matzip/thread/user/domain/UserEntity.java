package com.matzip.thread.user.domain;

import com.matzip.thread.role.domain.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserEntity {
    private final String username;
    private final String nickname;
    private final String password;
    private final Role role;
}
