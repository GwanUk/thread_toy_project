package com.matzip.thread.user.adapter.out_;

import com.matzip.thread.common.exception.NotFoundDataException;
import com.matzip.thread.user.domain.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.matzip.thread.role.domain.Role.ROLE_ADMIN;
import static com.matzip.thread.role.domain.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(UserPersistenceAdapter.class)
class UserPersistenceAdapterTest {

    @Autowired
    private UserPersistenceAdapter userPersistenceAdapter;

    @Test
    @Sql("/sql/user/user-data.sql")
    @DisplayName("전체 조회")
    void findAll() {
        // when
        List<UserEntity> entities = userPersistenceAdapter.findAll();

        // then
        assertThat(entities).hasSize(4);
    }

    @Test
    @Sql("/sql/user/user-data.sql")
    @DisplayName("전체 조회 속성 확인")
    void findAll_check() {
        // when
        List<UserEntity> entities = userPersistenceAdapter.findAll();

        // then
        assertThat(entities.get(0).getUsername()).isEqualTo("user01");
        assertThat(entities.get(0).getNickname()).isEqualTo("관리자");
        assertThat(entities.get(0).getPassword()).isEqualTo("1234");
        assertThat(entities.get(0).getRole()).isEqualTo(ROLE_ADMIN);
    }

    @Test
    @Sql("/sql/user/user-data.sql")
    @DisplayName("단건 조회")
    void findByUser() {
        // when
        UserEntity entity = userPersistenceAdapter.findByUsername("user01").orElseThrow(() -> new RuntimeException("데이터를 찾을 수 없습니다."));

        // then
        assertThat(entity.getUsername()).isEqualTo("user01");
        assertThat(entity.getNickname()).isEqualTo("관리자");
        assertThat(entity.getPassword()).isEqualTo("1234");
        assertThat(entity.getRole()).isEqualTo(ROLE_ADMIN);
    }

    @Test
    @Sql("/sql/user/user-table.sql")
    @DisplayName("저장")
    void save() {
        // given
        UserEntity entity = new UserEntity("user01", "관리자", "1234", ROLE_USER);

        // when
        userPersistenceAdapter.save(entity);

        // then
        List<UserEntity> entities = userPersistenceAdapter.findAll();
        assertThat(entities).hasSize(1);
    }

    @Test
    @Sql("/sql/user/user-table.sql")
    @DisplayName("저장 속성 확인")
    void save_check() {
        // given
        UserEntity entity = new UserEntity("user01", "관리자", "1234", ROLE_USER);

        // when
        userPersistenceAdapter.save(entity);

        // then
        List<UserEntity> entities = userPersistenceAdapter.findAll();
        assertThat(entities.get(0).getUsername()).isEqualTo("user01");
        assertThat(entities.get(0).getNickname()).isEqualTo("관리자");
        assertThat(entities.get(0).getPassword()).isEqualTo("1234");
        assertThat(entities.get(0).getRole()).isEqualTo(ROLE_USER);
    }

    @Test
    @Sql("/sql/user/user-data.sql")
    @DisplayName("갱신")
    void update() {
        // given
        UserEntity savedEntity = new UserEntity("user01", "유저", "1111", ROLE_USER);

        // when
        userPersistenceAdapter.update("user01", savedEntity);

        // then
        UserEntity entity = userPersistenceAdapter.findByUsername("user01").orElseThrow(() -> new NotFoundDataException("데이터를 찾을 수 없습니다."));
        assertThat(entity.getUsername()).isEqualTo("user01");
        assertThat(entity.getNickname()).isEqualTo("유저");
        assertThat(entity.getPassword()).isEqualTo("1111");
        assertThat(entity.getRole()).isEqualTo(ROLE_USER);
    }

    @Test
    @Sql("/sql/user/user-data.sql")
    @Commit
    @DisplayName("삭제")
    void delete() {
        // when
        userPersistenceAdapter.delete("user01");

        // then
        List<String> usernames = userPersistenceAdapter.findAll().stream()
                .map(UserEntity::getUsername)
                .toList();
        assertThat(usernames).doesNotContain("user01");
    }
}