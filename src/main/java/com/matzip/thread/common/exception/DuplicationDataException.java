package com.matzip.thread.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicationDataException extends ApplicationException {

    private static final String msg = "Already exists: ";

    public DuplicationDataException(String message) {
        super(msg + message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
