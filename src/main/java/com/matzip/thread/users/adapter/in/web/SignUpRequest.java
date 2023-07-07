package com.matzip.thread.users.adapter.in.web;

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

    public User toDomainEntity(PasswordEncoder passwordEncoder) {
        return new User(
                username,
                nickname,
                passwordEncoder.encode(password),
                Role.USER
        );
    }
}
