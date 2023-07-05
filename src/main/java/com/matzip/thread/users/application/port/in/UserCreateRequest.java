package com.matzip.thread.users.application.port.in;

import com.matzip.thread.users.application.port.out.UserEntity;
import com.matzip.thread.users.domain.Membership;
import com.matzip.thread.users.domain.User;

public class UserCreateRequest {
    private final String username;
    private final String password;
    private final Membership membership;

    public UserCreateRequest(String username, String password, Membership membership) {
        this.username = username;
        this.password = password;
        this.membership = membership;
    }

    public User toDomainEntity() {
        return new User(
                username,
                password,
                membership
        );
    }

    public static UserCreateRequest formDomainEntity(User user) {
        return new UserCreateRequest(
                user.getUsername(),
                user.getPassword(),
                user.getMembership()
        );
    }
}
