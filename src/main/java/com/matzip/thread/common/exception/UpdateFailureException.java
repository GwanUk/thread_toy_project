package com.matzip.thread.common.exception;

import org.springframework.http.HttpStatus;

public class UpdateFailureException extends ApplicationException {
    public UpdateFailureException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
