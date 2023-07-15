package com.matzip.thread.common.exception.securityexception;

import org.springframework.security.access.AccessDeniedException;

public class AccessDeniedIpAddressException extends AccessDeniedException {

    public AccessDeniedIpAddressException(String msg) {
        super(msg);
    }

    public AccessDeniedIpAddressException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
