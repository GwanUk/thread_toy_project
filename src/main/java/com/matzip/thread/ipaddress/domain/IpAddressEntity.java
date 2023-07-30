package com.matzip.thread.ipaddress.domain;

import com.matzip.thread.common.exception.InvalidEntityException;
import com.matzip.thread.common.validator.Validator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class IpAddressEntity implements Validator {
    private final String ipAddress;

    @Override
    public void validate() {
        if (Objects.isNull(ipAddress) || ipAddress.length() == 0) {
            throw new InvalidEntityException("ipAddress is empty", "IpAddressEntity", "ipAddress", ipAddress);
        }
    }
}
