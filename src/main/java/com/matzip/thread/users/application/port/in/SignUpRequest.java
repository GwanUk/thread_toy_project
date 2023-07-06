package com.matzip.thread.users.application.port.in;

import com.matzip.thread.users.domain.Role;
import com.matzip.thread.users.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class SignUpRequest {
    private final String username;
    private final String password;
    private final Role role;

    public SignUpRequest(String username, String password, Role role) {
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

    public static SignUpRequest formDomainEntity(User user) {
        return new SignUpRequest(
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );
    }

    public SignUpRequest passwordEncode(PasswordEncoder passwordEncoder) {
        return new SignUpRequest(username, passwordEncoder.encode(password), role);
    }
}
