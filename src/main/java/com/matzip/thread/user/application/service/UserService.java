package com.matzip.thread.user.application.service;

import com.matzip.thread.role.application.prot.out_.RoleOutPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import com.matzip.thread.user.application.port.in.UserInPort;
import com.matzip.thread.user.application.port.out_.UserOutPort;
import com.matzip.thread.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
class UserService implements UserInPort {

    private final UserOutPort userOutPort;

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userOutPort.findByUsername(username);
    }

    @Override
    public void signUp(UserEntity userEntity, Role role) {
        userOutPort.save(userEntity, role);
    }
}
