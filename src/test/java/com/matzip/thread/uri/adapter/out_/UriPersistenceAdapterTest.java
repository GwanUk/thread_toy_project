package com.matzip.thread.uri.adapter.out_;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.uri.domain.UriEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@DataJpaTest
@Import(UriPersistenceAdapter.class)
class UriPersistenceAdapterTest {
    @Autowired
    private UriPersistenceAdapter uriPersistenceAdapter;

    @Test
    @Sql("/sql/uri/uri-repository-test-data.sql")
    @DisplayName("RUI 자원을 ROLE 권한과 함께 조회 성공")
    void findAllWithRoles() {
        // given
        // when
        List<UriEntity> uriList = uriPersistenceAdapter.findAllWithRoles();

        // then
        BDDAssertions.then(uriList.get(0).getUriName()).isEqualTo("/api/thread");
        BDDAssertions.then(uriList.get(0).getRoles().get(0).getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(uriList.get(0).getRoles().get(0).getDescription()).isEqualTo("유저 권한");

        BDDAssertions.then(uriList.get(1).getUriName()).isEqualTo("/api/admin");
        BDDAssertions.then(uriList.get(1).getRoles().get(0).getRole()).isEqualTo(Role.ROLE_ADMIN);
        BDDAssertions.then(uriList.get(1).getRoles().get(0).getDescription()).isEqualTo("관리자 권한");
    }
}