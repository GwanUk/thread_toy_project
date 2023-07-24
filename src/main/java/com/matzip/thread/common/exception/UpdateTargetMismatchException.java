package com.matzip.thread.common.exception;

import org.springframework.http.HttpStatus;

public class UpdateTargetMismatchException extends ApplicationException {
    private static final String msg = "Update target is different";

    public UpdateTargetMismatchException(String Message) {
        super(msg + Message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.REQUEST_TIMEOUT;
    }
}
