package com.matzip.thread.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicationApplicationConvention extends ApplicationException {

    private static final String msg = "Already exists: ";

    public DuplicationApplicationConvention(String message) {
        super(msg + message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
