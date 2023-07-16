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

    @Query(nativeQuery = true, value =
            "  WITH r (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID) AS ( \n" +
            "SELECT ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID \n" +
            "  FROM ROLE \n" +
            " WHERE PARENT_ID IS NULL \n" +
            " UNION ALL \n" +
            "SELECT c.ROLE_ID, c.ROLE_NAME, c.DESCRIPTION, c.PARENT_ID \n" +
            "  FROM ROLE c \n" +
            "  JOIN r p \n" +
            "    ON c.PARENT_ID = p.ROLE_ID) \n" +
            "SELECT * FROM r")
    List<RoleJpaEntity> findAllChildren();

    //TODO findAllWithChildren
    //TODO findByRoleWithOptimisticLock
}
