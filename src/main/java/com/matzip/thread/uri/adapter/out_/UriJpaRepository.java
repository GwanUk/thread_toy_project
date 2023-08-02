package com.matzip.thread.uri.adapter.out_;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

interface UriJpaRepository extends JpaRepository<UriJpaEntity, Long> {
    @Query("select distinct u from UriJpaEntity u " +
            "left join fetch u.uriRolesJpaEntities ur " +
            "left join fetch ur.roleJpaEntity r " +
            "order by u.order")
    List<UriJpaEntity> findAllWithRoles();

    @Query("select distinct u from UriJpaEntity u " +
            "left join fetch u.uriRolesJpaEntities ur " +
            "left join fetch ur.roleJpaEntity r " +
            "where u.uri = :uri")
    Optional<UriJpaEntity> findByUri(String uri);

    @Modifying
    @Query("delete from UriJpaEntity u where u.uri = :uri")
    void deleteByUri(String uri);
}
