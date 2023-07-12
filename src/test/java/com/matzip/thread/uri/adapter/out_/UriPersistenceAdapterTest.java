package com.matzip.thread.uri.adapter.out_;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
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
    @DisplayName("uri 자원 디비 저장 성공")
    void save_uri() {
        // given
        UriEntity uriEntity1 = new UriEntity("/api/user/**", 1, List.of());
        UriEntity uriEntity2 = new UriEntity("/api/admin/**", 2, List.of());

        // when
        uriPersistenceAdapter.save(uriEntity1);
        uriPersistenceAdapter.save(uriEntity2);

        // then
        List<UriEntity> findUriEntities = uriPersistenceAdapter.findAllWithRoles();
        BDDAssertions.then(findUriEntities.get(0).getUriName()).isEqualTo("/api/user/**");
        BDDAssertions.then(findUriEntities.get(0).getUriOrder()).isEqualTo(1);
        BDDAssertions.then(findUriEntities.get(0).getRoles()).isEmpty();
        BDDAssertions.then(findUriEntities.get(1).getUriName()).isEqualTo("/api/admin/**");
        BDDAssertions.then(findUriEntities.get(1).getUriOrder()).isEqualTo(2);
        BDDAssertions.then(findUriEntities.get(1).getRoles()).isEmpty();
    }

    @Test
    @DisplayName("uri with role 자원 디비 저장 성공")
    void save_uri_role() {
        // given
        UriEntity uriEntity1 = new UriEntity("/api/user/**", 1, List.of(new RoleEntity(Role.ROLE_USER, "유저 권한")));
        UriEntity uriEntity2 = new UriEntity("/api/admin/**", 2, List.of(new RoleEntity(Role.ROLE_ADMIN, "관리자 권한")));

        // when
        uriPersistenceAdapter.save(uriEntity1);
        uriPersistenceAdapter.save(uriEntity2);

        // then
        List<UriEntity> findUriEntities = uriPersistenceAdapter.findAllWithRoles();
        BDDAssertions.then(findUriEntities.get(0).getUriName()).isEqualTo("/api/user/**");
        BDDAssertions.then(findUriEntities.get(0).getUriOrder()).isEqualTo(1);
        BDDAssertions.then(findUriEntities.get(0).getRoles().get(0).getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(findUriEntities.get(0).getRoles().get(0).getDescription()).isEqualTo("유저 자원");
        BDDAssertions.then(findUriEntities.get(1).getUriName()).isEqualTo("/api/admin/**");
        BDDAssertions.then(findUriEntities.get(1).getUriOrder()).isEqualTo(2);
        BDDAssertions.then(findUriEntities.get(1).getRoles().get(0).getRole()).isEqualTo(Role.ROLE_ADMIN);
        BDDAssertions.then(findUriEntities.get(1).getRoles().get(0).getDescription()).isEqualTo("관리자 자원");
    }

    @Test
    @Sql("/sql/uri/uri-repository-test-data.sql")
    @DisplayName("Uri 자원을 ROLE 권한과 함께 디비 조회 성공")
    void findAllWithRoles() {
        // given
        // when
        List<UriEntity> findUriEntities = uriPersistenceAdapter.findAllWithRoles();

        // then
        BDDAssertions.then(findUriEntities.get(0).getUriName()).isEqualTo("/api/thread");
        BDDAssertions.then(findUriEntities.get(0).getUriOrder()).isEqualTo(1);
        BDDAssertions.then(findUriEntities.get(0).getRoles().get(0).getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(findUriEntities.get(0).getRoles().get(0).getDescription()).isEqualTo("유저 권한");

        BDDAssertions.then(findUriEntities.get(1).getUriName()).isEqualTo("/api/admin");
        BDDAssertions.then(findUriEntities.get(1).getUriOrder()).isEqualTo(2);
        BDDAssertions.then(findUriEntities.get(1).getRoles().get(0).getRole()).isEqualTo(Role.ROLE_ADMIN);
        BDDAssertions.then(findUriEntities.get(1).getRoles().get(0).getDescription()).isEqualTo("관리자 권한");
    }
}