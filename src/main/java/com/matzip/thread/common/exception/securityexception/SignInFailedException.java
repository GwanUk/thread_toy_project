package com.matzip.thread.common.exception.securityexception;

import org.springframework.security.core.AuthenticationException;

public class SignInFailedException extends AuthenticationException {
    public SignInFailedException() {
        super("non-existent user or a wrong password");
    }

    public SignInFailedException(String msg) {
        super(msg);
    }

    public SignInFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
