package com.matzip.thread.uri.adapter.out_;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface UriJpaRepository extends JpaRepository<UriJpaEntity, Long> {
    @Query("select u from UriJpaEntity u" +
            " left join fetch u.uriRolesJpaEntities ur" +
            " left join fetch ur.roleJpaEntity r" +
            " order by u.uriOrder")
    List<UriJpaEntity> findAllWithRoles();
}
