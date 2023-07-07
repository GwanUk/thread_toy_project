package com.matzip.thread.users.adapter.in.web;

import com.matzip.thread.users.domain.Role;
import com.matzip.thread.users.domain.User;
import lombok.Getter;

@Getter
class UserResponse {
    private final String username;
    private final String nickname;
    private final String password;
    private final Role role;

    UserResponse(String username, String nickname, String password, Role role) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    static UserResponse fromDomainEntity(User user) {
        return new UserResponse(
                user.getUsername(),
                user.getNickname(),
                user.getPassword(),
                user.getRole()
        );
    }
}
