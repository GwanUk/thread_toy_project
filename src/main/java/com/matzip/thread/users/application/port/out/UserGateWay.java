package com.matzip.thread.users.application.port.out;

import com.matzip.thread.users.domain.User;

import java.util.List;

public interface UserGateWay {
    User findById(long id);

    User findByUsername(String username);

    List<User> findAll();

    long save(User user);

    long update(long id, User user);

    void delete(long id);
}
