package com.matzip.thread.uri.adapter.out_;

import com.matzip.thread.common.exception.NullArgumentException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThat(entities.size()).isEqualTo(5);

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
        assertThat(entities.get(4).getRoles().get(1)).isEqualTo(ROLE_VIP);
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


    @Test
    @Sql("/sql/uri/uri-data.sql")
    @DisplayName("uri 저장")
    void save_uri() {
        // given
        UriEntity entity = new UriEntity("/api/uri/test", 0, List.of(ROLE_ADMIN, ROLE_MANAGER));

        // when
        uriPersistenceAdapter.save(entity);

        // then
        List<UriEntity> entities = uriPersistenceAdapter.findAllWithRoles();
        assertThat(entities.size()).isEqualTo(6);
        assertThat(entities.get(0).getUri()).isEqualTo("/api/uri/test");
        assertThat(entities.get(0).getOrder()).isEqualTo(0);
        assertThat(entities.get(0).getRoles().get(0)).isEqualTo(ROLE_ADMIN);
        assertThat(entities.get(0).getRoles().get(1)).isEqualTo(ROLE_MANAGER);
    }

    @Test
    @Sql("/sql/uri/uri-data.sql")
    @DisplayName("null 저장")
    void save_null() {
        // given
        UriEntity entity = new UriEntity(null, 0, List.of(ROLE_ADMIN, ROLE_MANAGER));

        // when
        assertThatThrownBy(() -> uriPersistenceAdapter.save(entity))
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("Argument is empty: uri");
    }

    @Test
    @Sql("/sql/uri/uri-data.sql")
    @DisplayName("빈 문자열 저장")
    void save_empty() {
        // given
        UriEntity entity = new UriEntity("", 0, List.of(ROLE_ADMIN, ROLE_MANAGER));

        // when
        assertThatThrownBy(() -> uriPersistenceAdapter.save(entity))
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("Argument is empty: uri");
    }

    @Test
    @Sql("/sql/uri/uri-data.sql")
    @DisplayName("공백 저장")
    void save_blank() {
        // given
        UriEntity entity = new UriEntity(" ", 0, List.of(ROLE_ADMIN, ROLE_MANAGER));

        // when
        assertThatThrownBy(() -> uriPersistenceAdapter.save(entity))
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("Argument is empty: uri");
    }

    @Test
    @Sql("/sql/uri/uri-data.sql")
    @DisplayName("갱신")
    void update() {
        // given
        UriEntity saveEntity = new UriEntity("/api/**", 0, List.of(ROLE_ADMIN, ROLE_MANAGER));

        // when
        uriPersistenceAdapter.update("/api/**", saveEntity);

        // then
        UriEntity entity = uriPersistenceAdapter.findByUriWithRoles("/api/**").orElseThrow(() -> new RuntimeException("데이터를 찾을 수 없습니다"));
        assertThat(entity.getUri()).isEqualTo("/api/**");
        assertThat(entity.getOrder()).isEqualTo(0);
        assertThat(entity.getRoles()).containsExactly(ROLE_ADMIN, ROLE_MANAGER);
    }
}