package com.matzip.thread.user.adapter.out_.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByUsername(String username);
}
