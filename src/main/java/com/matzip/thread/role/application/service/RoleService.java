package com.matzip.thread.role.application.service;

import com.matzip.thread.common.exception.InvalidRequest;
import com.matzip.thread.common.exception.NotfoundArgument;
import com.matzip.thread.role.application.prot.in.RoleInPort;
import com.matzip.thread.role.application.prot.out_.RoleOutPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class RoleService implements RoleInPort {

    private final RoleOutPort roleOutPort;

    @Override
    public RoleEntity findByRole(Role role) {
        return roleOutPort.findByRole(role).orElseThrow(() -> new NotfoundArgument(role.name()));
    }

    @Override
    public List<RoleEntity> findAll() {
        return roleOutPort.findAll();
    }

    @Override
    @Transactional
    public void save(RoleEntity roleEntity) {
        roleOutPort.save(roleEntity);
    }
}
