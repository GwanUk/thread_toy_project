package com.matzip.thread.users.application.port.in;

import com.matzip.thread.users.adapter.out.persistence.UserJpaEntity;
import com.matzip.thread.users.domain.Role;
import com.matzip.thread.users.domain.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private final String username;
    private final String nickname;
    private final String password;
    private final Role role;

    public UserResponse(String username, String nickname, String password, Role role) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    public static UserResponse fromDomainEntity(User user) {
        return new UserResponse(
                user.getUsername(),
                user.getNickname(),
                user.getPassword(),
                user.getRole()
        );
    }
}
