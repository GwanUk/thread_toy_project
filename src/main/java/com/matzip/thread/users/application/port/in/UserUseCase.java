package com.matzip.thread.users.application.port.in;

import com.matzip.thread.users.domain.User;

import java.util.Optional;

public interface UserUseCase {
    void signUp(SignUpRequest signUpRequest);

    void delete(Long id);

    User getByUsername(String username);

    Optional<User> findByUsername(String username);
}
