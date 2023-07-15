package com.matzip.thread.ipaddress.application.service;

import com.matzip.thread.ipaddress.application.port.in.IpAddressInPort;
import com.matzip.thread.ipaddress.application.port.out_.IpAddressOutPort;
import com.matzip.thread.ipaddress.domain.IpAddressEntity;
import com.matzip.thread.common.exception.NotFoundDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class IpAddressServiceAddress implements IpAddressInPort {

    private final IpAddressOutPort ipAddressOutPort;

    public IpAddressEntity findByIpAddress(String ipAddress) {
        return ipAddressOutPort.findByIpAddress(ipAddress)
                .orElseThrow(() -> new NotFoundDataException(ipAddress));
    }

    @Override
    public List<String> getIpAddresses() {
        return ipAddressOutPort.getIpAddresses();
    }

    @Override
    @Transactional
    public void save(String ipAddress) {
        ipAddressOutPort.save(ipAddress);
    }
}
