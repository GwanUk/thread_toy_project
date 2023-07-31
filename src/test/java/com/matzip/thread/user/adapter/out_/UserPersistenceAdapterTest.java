package com.matzip.thread.user.adapter.out_;

import com.matzip.thread.user.domain.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.matzip.thread.role.domain.Role.ROLE_ADMIN;
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
        assertThat(entities).hasSize(5);
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
}