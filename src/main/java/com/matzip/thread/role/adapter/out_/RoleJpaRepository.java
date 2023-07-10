package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.role.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, Long> {
    RoleJpaEntity findByRole(Role role);
}
