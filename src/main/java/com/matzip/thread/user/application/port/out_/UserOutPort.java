package com.matzip.thread.user.application.port.out_;

import com.matzip.thread.user.domain.UserEntity;

import java.util.Optional;

public interface UserOutPort {

    Optional<UserEntity> findByUsername(String username);

    void save(UserEntity userEntity);
}
