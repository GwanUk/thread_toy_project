package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.aop.ValidationAspect;
import com.matzip.thread.common.exception.DuplicationApplicationConvention;
import com.matzip.thread.common.exception.NotFoundDataException;
import com.matzip.thread.common.exception.NullArgumentException;
import com.matzip.thread.role.application.prot.out_.RolePersistencePort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Import({RolePersistenceAdapter.class,
        AopAutoConfiguration.class,
        ValidationAspect.class})
class RolePersistenceAdapterTest {

    @Autowired
    private RolePersistencePort rolePersistenceAdapter;

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("단건 조회")
    void findByRole() {
        // given
        // when
        RoleEntity findRoleEntity = rolePersistenceAdapter.findByRole(Role.ROLE_USER).orElseThrow(() -> new RuntimeException("해당 권한을 찾을 수 없습니다."));

        // then
        BDDAssertions.then(findRoleEntity.getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(findRoleEntity.getDescription()).isEqualTo("유저 권한");
        BDDAssertions.then(findRoleEntity.getParent()).isEqualTo(Role.ROLE_ADMIN);
        BDDAssertions.then(findRoleEntity.getChildren()).isEmpty();
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("단건 admin 조회")
    void findByRole_admin() {
        // given
        // when
        RoleEntity findRoleEntity = rolePersistenceAdapter.findByRole(Role.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("해당 권한을 찾을 수 없습니다."));

        // then
        BDDAssertions.then(findRoleEntity.getRole()).isEqualTo(Role.ROLE_ADMIN);
        BDDAssertions.then(findRoleEntity.getDescription()).isEqualTo("관리자 권한");
        BDDAssertions.then(findRoleEntity.getParent()).isNull();
        BDDAssertions.then(findRoleEntity.getChildren().get(0)).isEqualTo(Role.ROLE_USER);
    }

    @Test
    @DisplayName("DB에 존재하지 않는 권한 단건 조회. empty 반환")
    void findByRole_not_exist() {
        // expected
        BDDAssertions.then(rolePersistenceAdapter.findByRole(null)).isEqualTo(Optional.empty());
        BDDAssertions.then(rolePersistenceAdapter.findByRole(Role.ROLE_USER)).isEqualTo(Optional.empty());
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("전체 조회")
    void findAll() {
        // given
        // when
        List<RoleEntity> roleEntities = rolePersistenceAdapter.findAll();

        // then
        BDDAssertions.then(roleEntities.get(0).getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(roleEntities.get(0).getDescription()).isEqualTo("유저 권한");
        BDDAssertions.then(roleEntities.get(0).getParent()).isEqualTo(Role.ROLE_ADMIN);
        BDDAssertions.then(roleEntities.get(1).getRole()).isEqualTo(Role.ROLE_ADMIN);
        BDDAssertions.then(roleEntities.get(1).getDescription()).isEqualTo("관리자 권한");
    }

    @Test
    @DisplayName("데이터가 없는 상태에서 전체 조회. empty list 반환")
    void findAll_failure_no_data() {
        // expected
        BDDAssertions.then(rolePersistenceAdapter.findAll()).isEmpty();
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("권한만 등록")
    void save_only() {
        // given
        RoleEntity roleEntity = new RoleEntity(Role.ROLE_MANAGER, "매니저 권한", null, List.of());

        // when
        rolePersistenceAdapter.save(roleEntity);

        // then
        RoleEntity findRoleEntity = rolePersistenceAdapter.findByRole(Role.ROLE_MANAGER).orElseThrow(() -> new RuntimeException("해당 권한을 찾을 수 없습니다."));
        BDDAssertions.then(findRoleEntity.getRole()).isEqualTo(Role.ROLE_MANAGER);
        BDDAssertions.then(findRoleEntity.getDescription()).isEqualTo("매니저 권한");
        BDDAssertions.then(findRoleEntity.getParent()).isNull();
        BDDAssertions.then(findRoleEntity.getChildren()).isEmpty();
    }

    @Test
    @DisplayName("null 저장 시도. 예외 발생")
    void save_null() {
        // expected
        BDDAssertions.thenThrownBy(() -> rolePersistenceAdapter.save(null))
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("Argument is empty: RoleEntity");
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("권한 중복 등록 시도. 예외 발생")
    void save_duplication() {
        // given
        RoleEntity roleEntity = new RoleEntity(Role.ROLE_USER, "유저 권한", null, List.of());

        // expected
        BDDAssertions.thenThrownBy(() -> rolePersistenceAdapter.save(roleEntity))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("부모와 자식 가지고 권한 등록")
    void save_parent_children() {
        // given
        RoleEntity roleEntity = new RoleEntity(Role.ROLE_MANAGER, "매니저 권한", Role.ROLE_ADMIN, List.of(Role.ROLE_USER));

        // when
        rolePersistenceAdapter.save(roleEntity);

        // then
        RoleEntity findRoleEntity = rolePersistenceAdapter.findByRole(Role.ROLE_MANAGER).orElseThrow(() -> new RuntimeException("해당 권한을 찾을 수 없습니다."));
        BDDAssertions.then(findRoleEntity.getRole()).isEqualTo(Role.ROLE_MANAGER);
        BDDAssertions.then(findRoleEntity.getDescription()).isEqualTo("매니저 권한");
        BDDAssertions.then(findRoleEntity.getParent()).isEqualTo(Role.ROLE_ADMIN);
        BDDAssertions.then(findRoleEntity.getChildren().get(0)).isEqualTo(Role.ROLE_USER);
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("등록 되지 않은 부모를 가지고 권한 등록")
    void save_not_exist_parent() {
        // given
        RoleEntity roleEntity = new RoleEntity(Role.ROLE_VIP, "특급 권한", Role.ROLE_MANAGER, List.of());

        // expected
        BDDAssertions.thenThrownBy(() -> rolePersistenceAdapter.save(roleEntity))
                .isInstanceOf(NotFoundDataException.class)
                .hasMessageContaining("ROLE_MANAGER");
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("등록 되지 않은 자식을 가지고 권한 등록")
    void save_not_exist_child() {
        // given
        RoleEntity roleEntity = new RoleEntity(Role.ROLE_MANAGER, "매니저 권한", null, List.of(Role.ROLE_VIP));

        // expected
        BDDAssertions.thenThrownBy(() -> rolePersistenceAdapter.save(roleEntity))
                .isInstanceOf(NotFoundDataException.class)
                .hasMessageContaining("ROLE_VIP");
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("업데이트")
    void update() {
        // given
        RoleEntity roleEntity = new RoleEntity(Role.ROLE_MANAGER, "유저 to 매니저 권한", null, List.of());

        // when
        rolePersistenceAdapter.update(Role.ROLE_USER, roleEntity);

        // then
        RoleEntity findRoleEntity = rolePersistenceAdapter.findByRole(Role.ROLE_MANAGER).orElseThrow(() -> new RuntimeException("해당 권한을 찾을 수 없습니다."));
        BDDAssertions.then(findRoleEntity.getRole()).isEqualTo(Role.ROLE_MANAGER);
        BDDAssertions.then(findRoleEntity.getDescription()).isEqualTo("유저 to 매니저 권한");
        BDDAssertions.then(findRoleEntity.getParent()).isNull();
        BDDAssertions.then(findRoleEntity.getChildren()).isEmpty();
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("부모를 자식으로 바꿔서 등록")
    void update_parent_to_children() {
        // given
        RoleEntity roleEntity = new RoleEntity(Role.ROLE_USER, "유저 권한", null, List.of(Role.ROLE_ADMIN));

        // when
        rolePersistenceAdapter.update(Role.ROLE_USER, roleEntity);

        // then
        RoleEntity findRoleEntity = rolePersistenceAdapter.findByRole(Role.ROLE_USER).orElseThrow(() -> new RuntimeException("해당 권한을 찾을 수 없습니다."));
        BDDAssertions.then(findRoleEntity.getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(findRoleEntity.getDescription()).isEqualTo("유저 권한");
        BDDAssertions.then(findRoleEntity.getParent()).isNull();
        BDDAssertions.then(findRoleEntity.getChildren().get(0)).isEqualTo(Role.ROLE_ADMIN);
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("자식을 부모로 바꿔서 등록")
    void update_children_to_parent() {
        // given
        RoleEntity roleEntity = new RoleEntity(Role.ROLE_ADMIN, "관리자 권한", Role.ROLE_USER, List.of());

        // when
        rolePersistenceAdapter.update(Role.ROLE_ADMIN, roleEntity);

        // then
        RoleEntity findRoleEntity = rolePersistenceAdapter.findByRole(Role.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("해당 권한을 찾을 수 없습니다."));
        BDDAssertions.then(findRoleEntity.getRole()).isEqualTo(Role.ROLE_ADMIN);
        BDDAssertions.then(findRoleEntity.getDescription()).isEqualTo("관리자 권한");
        BDDAssertions.then(findRoleEntity.getParent()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(findRoleEntity.getChildren()).isEmpty();
    }

    @Test
    @DisplayName("권한을 null 업데이트 시도. 예외 발생")
    void update_null() {
        // expected
        BDDAssertions.thenThrownBy(() -> rolePersistenceAdapter.update(Role.ROLE_USER, null))
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("Argument is empty: RoleEntity");
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("이미 등록된 권한으로 업데이트 시도. 예외 발생")
    void update_exist_role() {
        // given
        RoleEntity roleEntity = new RoleEntity(Role.ROLE_ADMIN, "관리자 권한", null, List.of());

        // expected
        BDDAssertions.thenThrownBy(() -> rolePersistenceAdapter.update(Role.ROLE_USER, roleEntity))
                .isInstanceOf(DuplicationApplicationConvention.class)
                .hasMessage("Already exists: ROLE_ADMIN");
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("등록 되지 않은 부모로 업데이트 시도. 예외 발생")
    void update_not_exist_parent() {
        // given
        RoleEntity roleEntity = new RoleEntity(Role.ROLE_USER, "매니저 권한", Role.ROLE_MANAGER, List.of());

        // expected
        BDDAssertions.thenThrownBy(() -> rolePersistenceAdapter.update(Role.ROLE_USER, roleEntity))
                .isInstanceOf(NotFoundDataException.class)
                .hasMessage(Role.ROLE_MANAGER.name());
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("등록 되지 않은 자식으로 업데이트 시도. 예외 발생")
    void update_not_exist_children() {
        // given
        RoleEntity roleEntity = new RoleEntity(Role.ROLE_USER, "매니저 권한", null, List.of(Role.ROLE_MANAGER));

        // expected
        BDDAssertions.thenThrownBy(() -> rolePersistenceAdapter.update(Role.ROLE_USER, roleEntity))
                .isInstanceOf(NotFoundDataException.class)
                .hasMessage("[ROLE_MANAGER]");
    }
}