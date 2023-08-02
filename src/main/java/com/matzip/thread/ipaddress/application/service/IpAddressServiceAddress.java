package com.matzip.thread.ipaddress.application.service;

import com.matzip.thread.ipaddress.application.port.in.IpAddressWebPort;
import com.matzip.thread.ipaddress.application.port.out_.IpAddressPersistencePort;
import com.matzip.thread.ipaddress.domain.IpAddressEntity;
import com.matzip.thread.common.exception.NotFoundDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class IpAddressServiceAddress implements IpAddressWebPort {

    private final IpAddressPersistencePort ipAddressPersistencePort;

    public IpAddressEntity findByIpAddress(String ipAddress) {
        return ipAddressPersistencePort.findByIpAddress(ipAddress)
                .orElseThrow(() -> new NotFoundDataException(ipAddress));
    }

    @Override
    public List<String> getIpAddresses() {
        return ipAddressPersistencePort.getIpAddresses();
    }

    @Override
    @Transactional
    public void save(IpAddressEntity ipAddressEntity) {
        ipAddressPersistencePort.save(ipAddressEntity);
    }
}
