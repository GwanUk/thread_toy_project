package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.exception.InfiniteLoopException;
import com.matzip.thread.common.exception.NotFoundDataException;
import com.matzip.thread.common.exception.UpdateTargetMismatchException;
import com.matzip.thread.role.application.prot.out_.RolePersistencePort;
import com.matzip.thread.role.domain.RoleEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static com.matzip.thread.role.domain.Role.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import({RolePersistenceAdapter.class,
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
    @Sql("/sql/role/role-data.sql")
    @DisplayName("차상위 권한 갱신. 업데이트 대상 권한의 부모가 변경되면 안됨")
    void update_second() {
        // given
        RoleEntity vip = new RoleEntity(ROLE_VIP, "ROLE_VIP", List.of());
        RoleEntity user = new RoleEntity(ROLE_USER, "ROLE_USER", List.of(vip));
        RoleEntity manager = new RoleEntity(ROLE_MANAGER, "ROLE_MANAGER", List.of(user));

        // when
        rolePersistenceAdapter.update(ROLE_MANAGER, manager);

        // then
        List<RoleEntity> entities = rolePersistenceAdapter.findAll();
        assertThat(entities.get(0).getRole()).isEqualTo(ROLE_ADMIN);
        assertThat(entities.get(0).getDescription()).isEqualTo("관리자 권한");
        assertThat(entities.get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_MANAGER);
        assertThat(entities.get(0).getChildren().get(0).getDescription()).isEqualTo("ROLE_MANAGER");
        assertThat(entities.get(0).getChildren().get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_USER);
        assertThat(entities.get(0).getChildren().get(0).getChildren().get(0).getDescription()).isEqualTo("ROLE_USER");
        assertThat(entities.get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_VIP);
        assertThat(entities.get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0).getDescription()).isEqualTo("ROLE_VIP");
    }

    @Test
    @Sql("/sql/role/role-data.sql")
    @DisplayName("권한 순환 참조 싸이클 갱신")
    void update_recursive() {
        // given
        RoleEntity manager = new RoleEntity(ROLE_MANAGER, "매니저 권한", List.of());
        RoleEntity vip = new RoleEntity(ROLE_VIP, "특급 권한", List.of(manager));

        // expected
        assertThatThrownBy(() -> rolePersistenceAdapter.update(ROLE_VIP, vip))
                .isInstanceOf(InfiniteLoopException.class);
    }

    @Test
    @Sql("/sql/role/role-table.sql")
    @DisplayName("자식 추가 갱신")
    void update_children() {
        // given
        RoleEntity user = new RoleEntity(ROLE_USER, "ROLE_USER", List.of());
        RoleEntity vip = new RoleEntity(ROLE_VIP, "ROLE_VIP", List.of());
        RoleEntity manager = new RoleEntity(ROLE_MANAGER, "ROLE_MANAGER", List.of());
        RoleEntity admin = new RoleEntity(ROLE_ADMIN, "ROLE_ADMIN", List.of());
        rolePersistenceAdapter.save(admin);
        rolePersistenceAdapter.save(manager);
        rolePersistenceAdapter.save(vip);
        rolePersistenceAdapter.save(user);

        // when
        admin.addChild(manager);
        manager.addChild(vip);
        manager.addChild(user);
        rolePersistenceAdapter.update(ROLE_ADMIN, admin);

        // then
        RoleEntity entity = rolePersistenceAdapter.findByRole(ROLE_ADMIN).orElseThrow(() -> new RuntimeException("해당 권한을 찾을 수 없습니다."));
        assertThat(entity.getRole()).isEqualTo(ROLE_ADMIN);
        assertThat(entity.getChildren().get(0).getRole()).isEqualTo(ROLE_MANAGER);
        assertThat(entity.getChildren().get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_VIP);
        assertThat(entity.getChildren().get(0).getChildren().get(1).getRole()).isEqualTo(ROLE_USER);
    }

    @Test
    @Sql("/sql/role/role-data.sql")
    @DisplayName("자식 삭제 갱신")
    void update_children_remove() {
        // given
        RoleEntity admin = new RoleEntity(ROLE_ADMIN, "관리자 권한", List.of());

        // when
        rolePersistenceAdapter.update(ROLE_ADMIN, admin);

        // then
        List<RoleEntity> entities = rolePersistenceAdapter.findAll();
        assertWith(entities.size()).isEqualTo(2);
        assertThat(entities.get(0).getRole()).isEqualTo(ROLE_ADMIN);
        assertThat(entities.get(1).getRole()).isEqualTo(ROLE_MANAGER);
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
    @DisplayName("DB에 없는 권한 갱신")
    void update_not_exist() {
        // given
        RoleEntity admin = new RoleEntity(ROLE_ADMIN, "관리자 권한", List.of());

        // expected
        BDDAssertions.thenThrownBy(() -> rolePersistenceAdapter.update(ROLE_ADMIN, admin))
                .isInstanceOf(NotFoundDataException.class)
                .hasMessage("Doesn't exists data: ROLE_ADMIN");
    }

    @Test
    @Sql("/sql/role/role-table.sql")
    @DisplayName("DB에 없는 자식 추가 갱신")
    void update_not_exist_children() {
        // given
        RoleEntity save = new RoleEntity(ROLE_ADMIN, "관리자", List.of());
        rolePersistenceAdapter.save(save);

        RoleEntity user = new RoleEntity(ROLE_USER, "ROLE_USER", List.of());
        RoleEntity admin = new RoleEntity(ROLE_ADMIN, "ROLE_ADMIN", List.of(user));

        // expected
        assertThatThrownBy(() -> rolePersistenceAdapter.update(ROLE_ADMIN, admin))
                .isInstanceOf(NotFoundDataException.class)
                .hasMessage("Doesn't exists data: 1 data");
    }

    @Test
    @Sql("/sql/role/role-data.sql")
    @DisplayName("삭제")
    void delete() {
        // when
        rolePersistenceAdapter.delete(ROLE_ADMIN);

        // then
        Optional<RoleEntity> entity = rolePersistenceAdapter.findByRole(ROLE_ADMIN);
        assertThat(entity).isEqualTo(Optional.empty());

        List<RoleEntity> entities = rolePersistenceAdapter.findAll();
        assertThat(entities.get(0).getRole()).isEqualTo(ROLE_MANAGER);
        assertThat(entities.get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_VIP);
        assertThat(entities.get(0).getChildren().get(0).getChildren().get(0).getRole()).isEqualTo(ROLE_USER);
        assertThat(entities.get(0).getChildren().get(0).getChildren().get(0).getChildren()).isEmpty();
    }

    @Test
    @Sql("/sql/role/role-data.sql")
    @DisplayName("자식 권한 삭제")
    void delete_children() {
        // when
        rolePersistenceAdapter.delete(ROLE_MANAGER);

        // then
        RoleEntity entity = rolePersistenceAdapter.findByRole(ROLE_ADMIN).orElseThrow(() -> new RuntimeException("권한을 찾을 수 없습니다."));
        assertThat(entity.getChildren()).isEmpty();
    }

    @Test
    @Sql("/sql/role/role-table.sql")
    @DisplayName("DB에 없는 권한 삭제")
    void delete_not_exist() {
        // expected
        rolePersistenceAdapter.delete(ROLE_ADMIN);
    }
}