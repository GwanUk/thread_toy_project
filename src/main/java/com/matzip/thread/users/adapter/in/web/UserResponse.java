package com.matzip.thread.users.adapter.in.web;

import com.matzip.thread.users.domain.Role;
import com.matzip.thread.users.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class UserResponse {
    private final String username;
    private final String nickname;
    private final String password;
    private final Role role;

    static UserResponse fromDomainEntity(User user) {
        return new UserResponse(
                user.getUsername(),
                user.getNickname(),
                user.getPassword(),
                user.getRole()
        );
    }
}
