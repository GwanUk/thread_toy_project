package com.matzip.thread.users.application.port.out_;

import com.matzip.thread.users.domain.User;

import java.util.Optional;

public interface UserOutPort {

    Optional<User> findByUsername(String username);

    void save(User user);
}
