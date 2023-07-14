package com.matzip.thread.ipaddress.adapter.out_;

import com.matzip.thread.ipaddress.application.port.out_.IpAddressOutPort;
import com.matzip.thread.ipaddress.domain.IpAddressEntity;
import com.matzip.thread.common.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
class IpAddressPersistenceAdapterAddress implements IpAddressOutPort {

    private final IpAddressJpaRepository ipAddressJpaRepository;

    @Override
    public Optional<IpAddressEntity> findByIpAddress(String ipAddress) {
        return ipAddressJpaRepository.findByIpAddress(ipAddress)
                .map(IpAddressJpaEntity::toEntity);
    }

    @Override
    public List<String> getIpAddresses() {
        return ipAddressJpaRepository.findAll().stream()
                .map(IpAddressJpaEntity::getIpAddress)
                .toList();
    }

    @Override
    public void save(String ipAddress) {
        ipAddressJpaRepository.save(new IpAddressJpaEntity(ipAddress));
    }
}
