package com.matzip.thread.users.adapter.out.persistence;

import com.matzip.thread.users.application.port.out.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
