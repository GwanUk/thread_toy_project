package com.matzip.thread.ipaddress.application.port.out_;

import com.matzip.thread.ipaddress.domain.IpAddressEntity;

import java.util.List;
import java.util.Optional;

public interface IpAddressOutPort {
    Optional<IpAddressEntity> findByIpAddress(String ipAddress);

    List<String> getIpAddresses();

    void save(String ipAddress);
}
