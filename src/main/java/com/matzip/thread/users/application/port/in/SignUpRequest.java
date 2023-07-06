package com.matzip.thread.users.application.port.in;

import com.matzip.thread.users.domain.Role;
import com.matzip.thread.users.domain.User;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class SignUpRequest {
    private final String userId;
    private final String username;
    private final String password;
    private final Role role;

    public SignUpRequest(String userId, String username, String password, Role role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User toDomainEntity() {
        return new User(
                userId,
                username,
                password,
                role
        );
    }

    public static SignUpRequest formDomainEntity(User user) {
        return new SignUpRequest(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );
    }

    public SignUpRequest passwordEncode(PasswordEncoder passwordEncoder) {
        return new SignUpRequest(userId, username, passwordEncoder.encode(password), role);
    }
}
