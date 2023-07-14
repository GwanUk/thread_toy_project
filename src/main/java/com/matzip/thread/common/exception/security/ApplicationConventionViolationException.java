package com.matzip.thread.common.exception.security;

import com.matzip.thread.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ApplicationConventionViolationException extends ApplicationException {

    public ApplicationConventionViolationException() {
    }

    public ApplicationConventionViolationException(String message) {
        super(message);
    }

    public ApplicationConventionViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationConventionViolationException(Throwable cause) {
        super(cause);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
