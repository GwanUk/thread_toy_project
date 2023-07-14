package com.matzip.thread.ipaddress.application.port.in;

import com.matzip.thread.ipaddress.domain.IpAddressEntity;

import java.util.List;

public interface IpAddressInPort {
    IpAddressEntity findByIpAddress(String ipAddress);

    List<String> getIpAddresses();

    void save(String ipAddress);
}
