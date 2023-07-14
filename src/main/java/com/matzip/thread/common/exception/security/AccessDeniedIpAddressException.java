package com.matzip.thread.common.exception.security;

import org.springframework.security.access.AccessDeniedException;

public class AccessDeniedIpAddressException extends AccessDeniedException {
    public AccessDeniedIpAddressException(String msg) {
        super(msg);
    }
}
