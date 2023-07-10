package com.matzip.thread.user.adapter.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
class SignUpRequest {
    @NotBlank
    private final String username;
    @NotBlank
    private final String nickname;
    @NotBlank
    private final String password;

    User toDomainEntity(PasswordEncoder passwordEncoder) {
        return new User(
                username,
                nickname,
                passwordEncoder.encode(password),
                Role.ROLE_USER
        );
    }
}
