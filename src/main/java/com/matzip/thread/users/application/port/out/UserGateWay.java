package com.matzip.thread.users.application.port.out;

import com.matzip.thread.users.domain.User;

import java.util.Optional;

public interface UserGateWay {

    Optional<User> findByUsername(String username);

    void save(User user);
}
