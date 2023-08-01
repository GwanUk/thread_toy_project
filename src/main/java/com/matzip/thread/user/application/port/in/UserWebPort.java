package com.matzip.thread.user.application.port.in;

import com.matzip.thread.user.domain.UserEntity;

import java.util.List;

public interface UserWebPort extends UserSecurityPort {
    void save(UserEntity userEntity);

    List<UserEntity> findAll();

    void update(String username, UserEntity userEntity);

    void delete(String username);
}
