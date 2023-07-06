package com.matzip.thread.users.application.port.in;

import com.matzip.thread.users.domain.Role;
import com.matzip.thread.users.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserCreateRequest {
    private final String username;
    private final String password;
    private final Role role;

    public UserCreateRequest(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User toDomainEntity() {
        return new User(
                username,
                password,
                role
        );
    }

    public static UserCreateRequest formDomainEntity(User user) {
        return new UserCreateRequest(
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );
    }

    public UserCreateRequest passwordEncode(PasswordEncoder passwordEncoder) {
        return new UserCreateRequest(username, passwordEncoder.encode(password), role);
    }
}
