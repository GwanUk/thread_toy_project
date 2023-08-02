package com.matzip.thread.ipaddress.application.port.out_;

import com.matzip.thread.ipaddress.domain.IpAddressEntity;

import java.util.List;
import java.util.Optional;

public interface IpAddressPersistencePort {
    Optional<IpAddressEntity> findByIpAddress(String ipAddress);

    List<String> getIpAddresses();

    void save(IpAddressEntity ipAddress);
}
