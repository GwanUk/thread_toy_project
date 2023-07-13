package com.matzip.thread.user.adapter.in;

import com.matzip.thread.user.domain.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class UserResponse {
    private final String username;
    private final String nickname;
    private final String password;
    private final String role;

    static UserResponse fromEntity(UserEntity userEntity) {
        return new UserResponse(
                userEntity.getUsername(),
                userEntity.getNickname(),
                userEntity.getPassword(),
                userEntity.getRole().name()
        );
    }
}
