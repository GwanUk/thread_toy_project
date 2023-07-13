package com.matzip.thread.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundDataException extends ApplicationException{

    public NotFoundDataException() {
    }

    public NotFoundDataException(String message) {
        super(message);
    }

    public NotFoundDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundDataException(Throwable cause) {
        super(cause);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
