package com.matzip.thread.common.exception;

import org.springframework.security.core.AuthenticationException;

public class ApiAuthenticationException extends AuthenticationException {
    public ApiAuthenticationException(String msg) {
        super(msg);
    }
}
