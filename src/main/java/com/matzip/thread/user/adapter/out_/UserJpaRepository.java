package com.matzip.thread.user.adapter.out_;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    @Query("select u from UserJpaEntity u left join fetch u.roleJpaEntity")
    List<UserJpaEntity> findAllWithRole();

    @Query("select u from UserJpaEntity u left join fetch u.roleJpaEntity where u.username = :username")
    Optional<UserJpaEntity> findByUsernameWithRole(String username);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from UserJpaEntity u where u.username = :username")
    void deleteByUsername(String username);
}
