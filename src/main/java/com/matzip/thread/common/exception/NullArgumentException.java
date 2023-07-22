package com.matzip.thread.common.exception;

import org.springframework.http.HttpStatus;

public class NullArgumentException extends ApplicationException {

    private static final String msg = "Argument is empty: ";

    public NullArgumentException(String message) {
        super(msg + message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
