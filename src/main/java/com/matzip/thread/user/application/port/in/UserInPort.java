package com.matzip.thread.user.application.port.in;

import com.matzip.thread.user.domain.User;

import java.util.Optional;

public interface UserInPort {
    Optional<User> findByUsername(String username);

    void signUp(User user);
}
