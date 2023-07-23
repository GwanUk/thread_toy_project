package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.role.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, Long> {
    Optional<RoleJpaEntity> findByRole(Role role);

    @Query("select r from RoleJpaEntity r where r.role in :roles")
    List<RoleJpaEntity> findInRoles(List<Role> roles);

    @Query("select r from RoleJpaEntity r join fetch r.children c")
    List<RoleJpaEntity> findAllChildren();
}
