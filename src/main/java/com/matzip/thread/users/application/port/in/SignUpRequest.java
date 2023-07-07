package com.matzip.thread.users.application.port.in;

import com.matzip.thread.users.domain.Role;
import com.matzip.thread.users.domain.User;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;

@Getter
public class SignUpRequest {
    @NotBlank
    private final String username;
    @NotBlank
    private final String nickname;
    @NotBlank
    private final String password;

    public SignUpRequest(String username, String nickname, String password) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
    }

    public User toDomainEntity() {
        return new User(
                username,
                nickname,
                password,
                Role.USER
        );
    }

    public SignUpRequest passwordEncode(PasswordEncoder passwordEncoder) {
        return new SignUpRequest(username, nickname, passwordEncoder.encode(password));
    }
}
