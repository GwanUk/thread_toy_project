package com.matzip.thread.user.application.port.in;

import com.matzip.thread.user.domain.UserEntity;

import java.util.List;

public interface UserWebPort extends UserSecurityPort {
    void signUp(UserEntity userEntity);

    List<UserEntity> findAll();
}
