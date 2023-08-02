package com.matzip.thread.ipaddress.application.port.in;

import com.matzip.thread.ipaddress.domain.IpAddressEntity;

public interface IpAddressWebPort extends IpAddressSecurityPort {
    void save(IpAddressEntity ipAddressEntity);
}
