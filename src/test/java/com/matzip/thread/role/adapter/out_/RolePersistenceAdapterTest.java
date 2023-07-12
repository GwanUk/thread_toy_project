package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@DataJpaTest
@Import(RolePersistenceAdapter.class)
class RolePersistenceAdapterTest {

    @Autowired
    private RolePersistenceAdapter rolePersistenceAdapter;

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("role 조회 by user 권한 성공")
    void findByRole() {
        // given
        // when
        RoleEntity findRoleEntity = rolePersistenceAdapter.findByRole(Role.ROLE_USER).orElseThrow(() -> new RuntimeException("해당 권한을 찾을 수 없습니다."));

        // then
        BDDAssertions.then(findRoleEntity.getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(findRoleEntity.getDescription()).isEqualTo("유저 권한");
        BDDAssertions.then(findRoleEntity.getParent()).isEqualTo(Role.ROLE_VIP);
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("권한 전체 조회 성공")
    void findAll() {
        // given
        // when
        List<RoleEntity> roleEntities = rolePersistenceAdapter.findAll();

        // then
        BDDAssertions.then(roleEntities.get(0).getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(roleEntities.get(0).getDescription()).isEqualTo("유저 권한");
        BDDAssertions.then(roleEntities.get(0).getParent()).isEqualTo(Role.ROLE_VIP);

        BDDAssertions.then(roleEntities.get(1).getRole()).isEqualTo(Role.ROLE_VIP);
        BDDAssertions.then(roleEntities.get(1).getDescription()).isEqualTo("특별 권한");
        BDDAssertions.then(roleEntities.get(1).getParent()).isEqualTo(Role.ROLE_VIP);

        BDDAssertions.then(roleEntities.get(2).getRole()).isEqualTo(Role.ROLE_ADMIN);
        BDDAssertions.then(roleEntities.get(2).getDescription()).isEqualTo("관리자 권한");
        BDDAssertions.then(roleEntities.get(2).getParent()).isEqualTo(Role.ROLE_ADMIN);
    }

    @Test
    @DisplayName("role user 권한 저장 persistence 성공")
    void save() {
        // given
        RoleEntity roleEntity = new RoleEntity(Role.ROLE_USER, "유저 권한", null);

        // when
        rolePersistenceAdapter.save(roleEntity);

        // then
        RoleEntity findRoleEntity = rolePersistenceAdapter.findByRole(Role.ROLE_USER).orElseThrow(() -> new RuntimeException("해당 권한을 찾을 수 없습니다."));;
        BDDAssertions.then(findRoleEntity.getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(findRoleEntity.getDescription()).isEqualTo("유저 권한");
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("부모 노드를 가진 권한 db 저장 성공")
    void role_has_parent() {
        // given
        RoleEntity roleEntity = new RoleEntity(Role.ROLE_MANAGER, "매니저 권한", Role.ROLE_ADMIN);

        // when
        rolePersistenceAdapter.save(roleEntity);

        // then
        RoleEntity findRoleEntity = rolePersistenceAdapter.findByRole(Role.ROLE_MANAGER).orElseThrow(() -> new RuntimeException("해당 권한을 찾을 수 없습니다."));
        BDDAssertions.then(findRoleEntity.getRole()).isEqualTo(Role.ROLE_MANAGER);
        BDDAssertions.then(findRoleEntity.getDescription()).isEqualTo("매니저 권한");
        BDDAssertions.then(findRoleEntity.getParent()).isEqualTo(Role.ROLE_ADMIN);
    }
}