package com.matzip.thread.common.Repository;

import com.matzip.thread.common.JpaEntity.ResourcesJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResourcesJpaRepository extends JpaRepository<ResourcesJpaEntity, Long> {

    @Query("select r from ResourcesJpaEntity r" +
            " join fetch r.resourcesRoles rr" +
            " join fetch rr.rolesJpaEntity ro" +
            " order by rr.orderNum")
    List<ResourcesJpaEntity> findAllWithRoles();
}
