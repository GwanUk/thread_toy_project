package com.matzip.thread.security;

import com.matzip.thread.common.JpaEntity.ResourcesJpaEntity;
import com.matzip.thread.common.JpaEntity.ResourcesRolesJpaEntity;
import com.matzip.thread.common.JpaEntity.RolesJpaEntity;
import com.matzip.thread.common.Repository.ResourcesRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class FakeResourcesRepository extends ResourcesRepository {
    public FakeResourcesRepository(EntityManager em) {
        super(em);
    }

    public List<ResourcesJpaEntity> findAllWithRoles() {
        ResourcesJpaEntity resource1 = new ResourcesJpaEntity(1L, "/api/thread");
        RolesJpaEntity role1 = new RolesJpaEntity(1L, "ROLE_USER", "유저 권한");
        ResourcesRolesJpaEntity resourcesRoles1 = new ResourcesRolesJpaEntity(1L, resource1, role1, 1);

        ResourcesJpaEntity resource2 = new ResourcesJpaEntity(2L, "/api/admin");
        RolesJpaEntity role2 = new RolesJpaEntity(2L, "ROLE_ADMIN", "관리자 권한");
        ResourcesRolesJpaEntity resourcesRoles2 = new ResourcesRolesJpaEntity(2L, resource2, role2, 1);

        return List.of(resource1, resource2);
    }
}
