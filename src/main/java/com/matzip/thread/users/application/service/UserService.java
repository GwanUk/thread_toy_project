package com.matzip.thread.users.application.service;

import com.matzip.thread.users.application.port.in.SignUpRequest;
import com.matzip.thread.users.application.port.in.UserUseCase;
import com.matzip.thread.users.application.port.out.UserGateWay;
import com.matzip.thread.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
class UserService implements UserUseCase {

    private final UserGateWay userGateWay;

    @Override
    public Optional<User> findByUsername(String id) {
        return Optional.empty();
    }

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        userGateWay.save(signUpRequest.toDomainEntity());
    }
}
