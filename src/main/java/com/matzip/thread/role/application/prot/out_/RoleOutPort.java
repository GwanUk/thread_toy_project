package com.matzip.thread.role.application.prot.out_;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;

import java.util.List;
import java.util.Optional;

public interface RoleOutPort {
    Optional<RoleEntity> findByRole(Role role);

    List<RoleEntity> findAll();

    void save(RoleEntity roleEntity);

    void update(Role role, RoleEntity roleEntity);

    void delete(Role role);
}
