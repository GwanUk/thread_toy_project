package com.matzip.thread.user.adapter.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.user.domain.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static java.util.Objects.nonNull;

@Getter
@RequiredArgsConstructor
class UserUpdateRequest {
    @NotBlank
    private final String nickname;
    @NotBlank
    private final String password;
    @NotNull
    private final Role role;

    UserEntity toEntity(PasswordEncoder passwordEncoder) {
        String encodedPassword = null;
        if (nonNull(password)) {
            encodedPassword = passwordEncoder.encode(password);
        }

        return new UserEntity(
                null,
                nickname,
                encodedPassword,
                role
        );
    }
}
