package com.matzip.thread.ipaddress.adapter.out_;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface IpAddressJpaRepository extends JpaRepository<IpAddressJpaEntity, Long> {
    Optional<IpAddressJpaEntity> findByIpAddress(String ipAddress);
}
