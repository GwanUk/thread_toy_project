package com.matzip.thread.role.application.prot.out_;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface RolePersistencePort {
    Optional<RoleEntity> findByRole(@NonNull Role role);

    List<RoleEntity> findAll();

    void save(@NonNull RoleEntity roleEntity);

    void update(@NonNull Role role, @NonNull RoleEntity roleEntity);

    void delete(@NonNull Role role);
}
