package com.matzip.thread.user.adapter.out_;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.user.domain.UserEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import(UserPersistenceAdapter.class)
class UserPersistenceAdapterTest {

    @Autowired
    private UserPersistenceAdapter userPersistenceAdapter;

    @Test
    @Sql("/sql/user/user-repository-test-data.sql")
    @DisplayName("username 으로 유저 단일 조회 성공")
    void findByUsername() {
        // given
        // when
        UserEntity findUserEntity = userPersistenceAdapter.findByUsername("user").orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        // then
        BDDAssertions.then(findUserEntity.getUsername()).isEqualTo("user");
        BDDAssertions.then(findUserEntity.getNickname()).isEqualTo("kim");
        BDDAssertions.then(findUserEntity.getPassword()).isEqualTo("1234");
        BDDAssertions.then(findUserEntity.getRole()).isEqualTo(Role.ROLE_USER);
    }

    @Test
    @Sql("/sql/role/role-repository-test-data.sql")
    @DisplayName("유저 영속화 성공")
    void save() {
        // given
        UserEntity userEntity = new UserEntity("jake", "yam", "4885", null);

        // when
        userPersistenceAdapter.save(userEntity, Role.ROLE_USER);

        // then
        UserEntity findUserEntity = userPersistenceAdapter.findByUsername("jake").orElseThrow(() -> new RuntimeException("존재 하지 않는 유저"));
        BDDAssertions.then(findUserEntity.getUsername()).isEqualTo("jake");
        BDDAssertions.then(findUserEntity.getNickname()).isEqualTo("yam");
        BDDAssertions.then(findUserEntity.getPassword()).isEqualTo("4885");
        BDDAssertions.then(findUserEntity.getRole()).isEqualTo(Role.ROLE_USER);
    }
}