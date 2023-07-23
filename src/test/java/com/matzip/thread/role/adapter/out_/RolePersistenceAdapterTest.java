package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.aop.ValidationAspect;
import com.matzip.thread.common.exception.UpdateFailureException;
import com.matzip.thread.common.exception.UpdateTargetMismatchException;
import com.matzip.thread.role.application.prot.out_.RolePersistencePort;
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

import static com.matzip.thread.role.domain.Role.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({RolePersistenceAdapter.class,
        AopAutoConfiguration.class,
        ValidationAspect.class,
        RoleJdbcTemplateRepository.class})
class RolePersistenceAdapterTest {

    @Autowired
    private RolePersistencePort rolePersistenceAdapter;

    @Test
    @Sql("/sql/role/role-data.sql")
    @DisplayName("전체 조회")
    void findAll() {
        // when
        List<RoleEntity> entity = rolePersistenceAdapter.findAll();

        // then
        assertThat(entity.get(0).getRole()).isEqualTo(ROLE_ADMIN);
        assertThat(entity.get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_MANAGER);
        assertThat(entity.get(0).getChildren().get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_VIP);
        assertThat(entity.get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_USER);
    }

    @Test
    @Sql("/sql/role/role-table.sql")
    @DisplayName("데이터가 없는 상태에서 전체 조회")
    void findAll_failure_no_data() {
        // expected
        BDDAssertions.then(rolePersistenceAdapter.findAll()).isEmpty();
    }

    @Test
    @Sql("/sql/role/role-data.sql")
    @DisplayName("단건 조회")
    void findByRole() {
        // when
        RoleEntity entity = rolePersistenceAdapter.findByRole(ROLE_ADMIN).orElseThrow(() -> new RuntimeException("해당 권한을 찾을 수 없습니다."));

        // then
        assertThat(entity.getRole()).isEqualTo(ROLE_ADMIN);
        assertThat(entity.getChildren().get(0).getRole()).isEqualTo(ROLE_MANAGER);
        assertThat(entity.getChildren().get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_VIP);
        assertThat(entity.getChildren().get(0).getChildren().get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_USER);
    }

    @Test
    @Sql("/sql/role/role-table.sql")
    @DisplayName("DB에 없는 권한 조회")
    void findByRole_not_exist() {
        // expected
        assertThat(rolePersistenceAdapter.findByRole(ROLE_USER)).isEqualTo(Optional.empty());
    }

    @Test
    @Sql("/sql/role/role-data.sql")
    @DisplayName("null 조회")
    void findByRole_null() {
        // expected
        assertThatThrownBy(() -> rolePersistenceAdapter.findByRole(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @Sql("/sql/role/role-table.sql")
    @DisplayName("권한 저장")
    void save() {
        // given
        RoleEntity user = new RoleEntity(ROLE_USER, "ROLE_USER", List.of());
        RoleEntity vip = new RoleEntity(ROLE_VIP, "ROLE_VIP", List.of(user));
        RoleEntity manager = new RoleEntity(ROLE_MANAGER, "ROLE_MANAGER", List.of(vip));
        RoleEntity admin = new RoleEntity(ROLE_ADMIN, "ROLE_ADMIN", List.of(manager));

        // when
        rolePersistenceAdapter.save(admin);

        // then
        RoleEntity entity = rolePersistenceAdapter.findByRole(ROLE_ADMIN).orElseThrow(() -> new RuntimeException("해당 권한을 찾을 수 없습니다."));
        assertThat(entity.getRole()).isEqualTo(ROLE_ADMIN);
        assertThat(entity.getChildren().get(0).getRole()).isEqualTo(ROLE_MANAGER);
        assertThat(entity.getChildren().get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_VIP);
        assertThat(entity.getChildren().get(0).getChildren().get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_USER);
    }

    @Test
    @DisplayName("null 저장")
    void save_null() {
        // expected
        assertThatThrownBy(() -> rolePersistenceAdapter.save(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("권한이 null 인 엔티티 저장")
    void save_role_null() {
        // given
        RoleEntity user = new RoleEntity(null, "ROLE_USER", List.of());
        assertThatThrownBy(() -> rolePersistenceAdapter.save(user))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @Sql("/sql/role/role-data.sql")
    @DisplayName("권한 중복 등록 시도. 예외 발생")
    void save_duplication() {
        // given
        RoleEntity entity = new RoleEntity(ROLE_USER, "ROLE_USER", List.of());

        // expected
        BDDAssertions.thenThrownBy(() -> rolePersistenceAdapter.save(entity))
                .isInstanceOf(DataIntegrityViolationException.class);
    }



    @Test
    @Sql("/sql/role/role-data.sql")
    @DisplayName("갱신")
    void update() {
        // given
        RoleEntity manager = new RoleEntity(ROLE_MANAGER, "ROLE_MANAGER", List.of());
        RoleEntity vip = new RoleEntity(ROLE_VIP, "ROLE_VIP", List.of(manager));
        RoleEntity user = new RoleEntity(ROLE_USER, "ROLE_USER", List.of(vip));
        RoleEntity admin = new RoleEntity(ROLE_ADMIN, "ROLE_ADMIN", List.of(user));

        // when
        rolePersistenceAdapter.update(ROLE_ADMIN, admin);

        // then
        List<RoleEntity> entities = rolePersistenceAdapter.findAll();
        assertThat(entities.get(0).getRole()).isEqualTo(ROLE_ADMIN);
        assertThat(entities.get(0).getDescription()).isEqualTo("ROLE_ADMIN");
        assertThat(entities.get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_USER);
        assertThat(entities.get(0).getChildren().get(0).getDescription()).isEqualTo("ROLE_USER");
        assertThat(entities.get(0).getChildren().get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_VIP);
        assertThat(entities.get(0).getChildren().get(0).getChildren().get(0).getDescription()).isEqualTo("ROLE_VIP");
        assertThat(entities.get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_MANAGER);
        assertThat(entities.get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0).getDescription()).isEqualTo("ROLE_MANAGER");
    }

    @Test
    @DisplayName("권한 null 갱신")
    void update_null() {
        // expected
        BDDAssertions.thenThrownBy(() -> rolePersistenceAdapter.update(ROLE_USER, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @Sql("/sql/role/role-data.sql")
    @DisplayName("업데이트 대상과 다른 권한 갱신")
    void update_exist_role() {
        // given
        RoleEntity admin = new RoleEntity(ROLE_ADMIN, "관리자 권한", List.of());

        // expected
        BDDAssertions.thenThrownBy(() -> rolePersistenceAdapter.update(ROLE_USER, admin))
                .isInstanceOf(UpdateTargetMismatchException.class)
                .hasMessage("Update target is different (ROLE_USER <> ROLE_ADMIN)");
    }

    @Test
    @Sql("/sql/role/role-table.sql")
    @DisplayName("등록 되지 않은 자식 추가 갱신")
    void update_not_exist_children() {
        // given
        RoleEntity savedUser = new RoleEntity(ROLE_USER, "ROLE_USER", List.of());
        rolePersistenceAdapter.save(savedUser);
        RoleEntity admin = new RoleEntity(ROLE_ADMIN, "ROLE_ADMIN", List.of());
        RoleEntity user = new RoleEntity(ROLE_USER, "ROLE_USER", List.of(admin));

        /*
         * ENUM Role 권한은 전부 다 DB에 저장되어 있어야 함.
         * DB에 저장되어 있지 않은 권한으로 업데이트를 시도할 경우
         * 업데이트 되지 않음.
         * 낙관적 락 재시도를 위한 예외가 발생함.
         */
        // expected
        assertThatThrownBy(() -> rolePersistenceAdapter.update(ROLE_USER, user)).isInstanceOf(UpdateFailureException.class);
    }
}