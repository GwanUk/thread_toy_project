package com.matzip.thread.user.application.port.in;

import com.matzip.thread.user.domain.UserEntity;

import java.util.Optional;

public interface UserQueryInPort {
    Optional<UserEntity> findByUsername(String username);
}
