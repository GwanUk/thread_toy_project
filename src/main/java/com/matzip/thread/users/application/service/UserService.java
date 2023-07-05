package com.matzip.thread.users.application.service;

import com.matzip.thread.users.application.port.in.UserCreateRequest;
import com.matzip.thread.users.application.port.in.UserUpdateRequest;
import com.matzip.thread.users.application.port.in.UserUseCase;
import com.matzip.thread.users.application.port.out.UserGateWay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserService implements UserUseCase {

    private final UserGateWay userGateWay;

    @Override
    public void join(UserCreateRequest userCreateRequest) {

    }

    @Override
    public void edit(Long id, UserUpdateRequest userUpdateRequest) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void login(Long id, String password) {

    }
}
