package com.matzip.thread.user.application.port.out_;

import com.matzip.thread.user.domain.User;

import java.util.Optional;

public interface UserOutPort {

    Optional<User> findByUsername(String username);

    void save(User user);
}
