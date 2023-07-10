package com.matzip.thread.user.adapter.out_;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.user.domain.User;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import(UserOutAdapter.class)
class UserOutAdapterTest {

    @Autowired
    private UserOutAdapter userOutAdapter;

    @Test
    @Sql("/sql/users/user-repository-test-data.sql")
    @DisplayName("username 으로 유저 단일 조회 성공")
    void findByUsername() {
        // given
        // when
        User user = userOutAdapter.findByUsername("user").orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        // then
        BDDAssertions.then(user.getUsername()).isEqualTo("user");
        BDDAssertions.then(user.getNickname()).isEqualTo("kim");
        BDDAssertions.then(user.getPassword()).isEqualTo("1234");
        BDDAssertions.then(user.getRole()).isEqualTo(Role.ROLE_USER);
    }

    @Test
    @DisplayName("유저 영속화 성공")
    void save() {
        // given
        User user = new User("user", "kim", "1234", Role.ROLE_USER);

        // when
        userOutAdapter.save(user);

        // then
        User findUser = userOutAdapter.findByUsername("user").orElseThrow(() -> new RuntimeException("존재 하지 않는 유저"));
        BDDAssertions.then(findUser.getUsername()).isEqualTo("user");
        BDDAssertions.then(findUser.getNickname()).isEqualTo("kim");
        BDDAssertions.then(findUser.getPassword()).isEqualTo("1234");
        BDDAssertions.then(findUser.getRole()).isEqualTo(Role.ROLE_USER);
    }
}