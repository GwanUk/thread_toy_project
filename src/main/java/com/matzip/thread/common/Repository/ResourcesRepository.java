package com.matzip.thread.common.Repository;

import com.matzip.thread.common.JpaEntity.ResourcesJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ResourcesRepository {

    private final EntityManager em;

    public List<ResourcesJpaEntity> findAllWithRoles() {
        return em.createQuery("select r from ResourcesJpaEntity r" +
                " join fetch r.resourcesRoles rr" +
                " join fetch rr.rolesJpaEntity ro" +
                " order by rr.orderNum", ResourcesJpaEntity.class).getResultList();
    }
}
