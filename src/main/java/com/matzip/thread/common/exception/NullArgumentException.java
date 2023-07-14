package com.matzip.thread.common.exception;

import org.springframework.http.HttpStatus;

public class NullArgumentException extends ApplicationException{
    public NullArgumentException() {
    }

    public NullArgumentException(String message) {
        super(message);
    }

    public NullArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullArgumentException(Throwable cause) {
        super(cause);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
