package com.matzip.thread.users.application.port.in;

import com.matzip.thread.users.domain.User;

import java.util.Optional;

public interface UserUseCase {
    Optional<User> findByUsername(String username);

    void signUp(User user);
}
