package com.matzip.thread.users.application.port.in;

import com.matzip.thread.users.domain.Role;
import com.matzip.thread.users.domain.User;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class SignUpRequest {
    private final String username;
    private final String nickname;
    private final String password;
    private final Role role;

    public SignUpRequest(String username, String nickname, String password, Role role) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    public User toDomainEntity() {
        return new User(
                username,
                nickname,
                password,
                role
        );
    }

    public static SignUpRequest formDomainEntity(User user) {
        return new SignUpRequest(
                user.getUsername(),
                user.getNickname(),
                user.getPassword(),
                user.getRole()
        );
    }

    public SignUpRequest passwordEncode(PasswordEncoder passwordEncoder) {
        return new SignUpRequest(username, nickname, passwordEncoder.encode(password), role);
    }
}
