package com.matzip.thread.users.adapter.out_.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UsersJpaRepository extends JpaRepository<UsersJpaEntity, Long> {

    Optional<UsersJpaEntity> findByUsername(String username);
}
