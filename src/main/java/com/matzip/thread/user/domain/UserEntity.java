package com.matzip.thread.user.domain;

import com.matzip.thread.role.domain.RoleEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserEntity {
    private final String username;
    private final String nickname;
    private final String password;
    private final RoleEntity roleEntity;
}
