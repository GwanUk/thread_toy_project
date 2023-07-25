package com.matzip.thread.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundDataException extends ApplicationException {

    private static final String msg = "Doesn't exists data: ";

    public NotFoundDataException(String message) {
        super(msg + message);
    }


    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
