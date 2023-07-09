package com.matzip.thread.common.Repository;

import com.matzip.thread.common.JpaEntity.ResourcesJpaEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@DataJpaTest
class ResourcesJpaRepositoryTest {
    @Autowired
    private ResourcesJpaRepository resourcesJpaRepository;

    @Test
    @Sql("/sql/common/resources-jpa-repository-test-data.sql")
    @DisplayName("리소스 엔티티 리스트 조회 join 리소스_롤 테이블 join 롤 테이블")
    void findAllWithRoles() {
        // given
        // when
        List<ResourcesJpaEntity> allWithRoles = resourcesJpaRepository.findAllWithRoles();

        // then
        BDDAssertions.then(allWithRoles.get(0).getId()).isEqualTo(1);
        BDDAssertions.then(allWithRoles.get(0).getUri()).isEqualTo("/api/thread");
        BDDAssertions.then(allWithRoles.get(0).getResourcesRoles().get(0).getRolesJpaEntity().getId()).isEqualTo(1);
        BDDAssertions.then(allWithRoles.get(0).getResourcesRoles().get(0).getRolesJpaEntity().getRoleName()).isEqualTo("ROLE_USER");
        BDDAssertions.then(allWithRoles.get(0).getResourcesRoles().get(0).getRolesJpaEntity().getDescription()).isEqualTo("유저 권한");

        BDDAssertions.then(allWithRoles.get(1).getId()).isEqualTo(2);
        BDDAssertions.then(allWithRoles.get(1).getUri()).isEqualTo("/api/admin");
        BDDAssertions.then(allWithRoles.get(1).getResourcesRoles().get(0).getRolesJpaEntity().getId()).isEqualTo(2);
        BDDAssertions.then(allWithRoles.get(1).getResourcesRoles().get(0).getRolesJpaEntity().getRoleName()).isEqualTo("ROLE_ADMIN");
        BDDAssertions.then(allWithRoles.get(1).getResourcesRoles().get(0).getRolesJpaEntity().getDescription()).isEqualTo("관리자 권한");
    }
}