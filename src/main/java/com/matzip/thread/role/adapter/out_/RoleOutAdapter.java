package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.role.application.prot.out_.RoleOutPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RoleOutAdapter implements RoleOutPort {

    private final RoleJpaRepository roleJpaRepository;

    @Override
    public RoleEntity findByRole(Role role) {
        return roleJpaRepository.findByRole(role).toEntity();
    }
}
