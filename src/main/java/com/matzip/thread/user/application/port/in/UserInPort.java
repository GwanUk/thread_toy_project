package com.matzip.thread.user.application.port.in;

import com.matzip.thread.user.domain.UserEntity;

import java.util.Optional;

public interface UserInPort {
    Optional<UserEntity> findByUsername(String username);

    void signUp(UserEntity userEntity);
}
