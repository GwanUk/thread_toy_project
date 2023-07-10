package com.matzip.thread.uri.adapter.out_;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface UriJapRepository extends JpaRepository<UriJpaEntity, Long> {
    @Query("select u from UriJpaEntity u" +
            " join fetch u.uriRolesJpaEntities ur" +
            " join fetch ur.roleJpaEntity r" +
            " order by u.uriOrder")
    List<UriJpaEntity> findAllWithRoles();
}
