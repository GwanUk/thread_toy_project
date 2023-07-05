package com.matzip.thread.users.application.port.in;

import com.matzip.thread.users.domain.Membership;
import com.matzip.thread.users.domain.User;

public class UserUpdateRequest {
    private final String username;
    private final String password;
    private final Membership membership;

    public UserUpdateRequest(String username, String password, Membership membership) {
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

    public static UserUpdateRequest formDomainEntity(User user) {
        return new UserUpdateRequest(
                user.getUsername(),
                user.getPassword(),
                user.getMembership()
        );
    }
}
