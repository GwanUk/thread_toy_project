package com.matzip.thread.users.application.service;

import com.matzip.thread.users.application.port.in.SignUpRequest;
import com.matzip.thread.users.application.port.in.UserUpdateRequest;
import com.matzip.thread.users.application.port.in.UserUseCase;
import com.matzip.thread.users.application.port.out.UserGateWay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class UserService implements UserUseCase {

    private final UserGateWay userGateWay;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        userGateWay.save(signUpRequest.toDomainEntity());
    }

    @Override
    public void edit(Long id, UserUpdateRequest userUpdateRequest) {

    }

    @Override
    public void delete(Long id) {

    }
}
