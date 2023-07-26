package com.matzip.thread.uri.adapter.out_;

import com.matzip.thread.uri.domain.UriEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static com.matzip.thread.role.domain.Role.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(UriPersistenceAdapter.class)
class UriPersistenceAdapterTest {
    @Autowired
    private UriPersistenceAdapter uriPersistenceAdapter;

    @Test
    @Sql("/sql/uri/uri-data.sql")
    @DisplayName("ROLE 과 함께 Uri 전체 조회")
    void findAllWithRoles() {
        // when
        List<UriEntity> entities = uriPersistenceAdapter.findAllWithRoles();

        // then
        assertThat(entities.get(0).getUri()).isEqualTo("/api/users/**");
        assertThat(entities.get(0).getOrder()).isEqualTo(1);
        assertThat(entities.get(0).getRoles().get(0)).isEqualTo(ROLE_ADMIN);

        assertThat(entities.get(1).getUri()).isEqualTo("/api/role/**");
        assertThat(entities.get(1).getOrder()).isEqualTo(2);
        assertThat(entities.get(1).getRoles().get(0)).isEqualTo(ROLE_MANAGER);

        assertThat(entities.get(2).getUri()).isEqualTo("/api/uri/**");
        assertThat(entities.get(2).getOrder()).isEqualTo(3);
        assertThat(entities.get(2).getRoles().get(0)).isEqualTo(ROLE_MANAGER);

        assertThat(entities.get(3).getUri()).isEqualTo("/api/ipaddress/**");
        assertThat(entities.get(3).getOrder()).isEqualTo(4);
        assertThat(entities.get(3).getRoles().get(0)).isEqualTo(ROLE_MANAGER);

        assertThat(entities.get(4).getUri()).isEqualTo("/api/**");
        assertThat(entities.get(4).getOrder()).isEqualTo(5);
        assertThat(entities.get(4).getRoles().get(0)).isEqualTo(ROLE_USER);
    }

    @Test
    @Sql("/sql/uri/uri-table.sql")
    @DisplayName("저장된 데이터가 없을 때 전체 조히")
    void findAllWithRoles_empty() {
        // when
        List<UriEntity> entities = uriPersistenceAdapter.findAllWithRoles();

        // then
        assertThat(entities).isEmpty();
    }

    @Test
    @Sql("/sql/uri/uri-data.sql")
    @DisplayName("ROLE 과 함께 Uri 단일 조회")
    void findByUriWithRoles() {
        // when
        UriEntity entity = uriPersistenceAdapter.findByUriWithRoles("/api/**")
                .orElseThrow(() -> new RuntimeException("존재하지 않는 데이터"));
        // then
        assertThat(entity.getUri()).isEqualTo("/api/**");
        assertThat(entity.getOrder()).isEqualTo(5);
        assertThat(entity.getRoles().get(0)).isEqualTo(ROLE_USER);
    }

    @Test
    @Sql("/sql/uri/uri-table.sql")
    @DisplayName("저장된 Uri 없을 때 조회")
    void findByUriWithRoles_empty() {
        // when
        Optional<UriEntity> entityOptional = uriPersistenceAdapter.findByUriWithRoles("");

        // then
        assertThat(entityOptional).isEqualTo(Optional.empty());
    }

    @Test
    @Sql("/sql/uri/uri-data.sql")
    @DisplayName("null 조회")
    void findByUriWithRoles_null() {
        // when
        Optional<UriEntity> entityOptional = uriPersistenceAdapter.findByUriWithRoles(null);

        // then
        assertThat(entityOptional).isEqualTo(Optional.empty());
    }

//
//    @Test
//    @DisplayName("uri 자원 디비 저장 성공")
//    void save_uri() {
//        // given
//        UriEntity uriEntity1 = new UriEntity("/api/user/**", 1, List.of());
//
//        // when
//        uriPersistenceAdapter.save(uriEntity1);
//
//        // then
//        List<UriEntity> findUriEntities = uriPersistenceAdapter.findAllWithRoles();
//        BDDAssertions.then(findUriEntities.get(0).getUri()).isEqualTo("/api/user/**");
//        BDDAssertions.then(findUriEntities.get(0).getOrder()).isEqualTo(1);
//        BDDAssertions.then(findUriEntities.get(0).getRoles()).isEmpty();
//    }
//
//    @Test
//    @Sql("/sql/role/role-data.sql")
//    @DisplayName("uri with role 자원 디비 저장 성공")
//    void save_uri_role() {
//        // given
//        UriEntity uriEntity1 = new UriEntity("/api/user/**", 1, List.of(Role.ROLE_USER, Role.ROLE_VIP));
//
//        // when
//        uriPersistenceAdapter.save(uriEntity1);
//
//        // then
//        List<UriEntity> findUriEntities = uriPersistenceAdapter.findAllWithRoles();
//        BDDAssertions.then(findUriEntities.get(0).getUri()).isEqualTo("/api/user/**");
//        BDDAssertions.then(findUriEntities.get(0).getOrder()).isEqualTo(1);
//        BDDAssertions.then(findUriEntities.get(0).getRoles().get(0)).isEqualTo(Role.ROLE_USER);
//    }
//

}