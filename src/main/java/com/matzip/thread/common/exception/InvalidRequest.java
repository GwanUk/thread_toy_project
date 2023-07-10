package com.matzip.thread.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequest extends ApplicationException{

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    private static final String ERROR_MESSAGE = "request is invalid";

    public InvalidRequest() {
        super(ERROR_MESSAGE);
    }

    public InvalidRequest(String message) {
        super(ERROR_MESSAGE + ": " + message);
    }

    public InvalidRequest(String message, Throwable cause) {
        super(ERROR_MESSAGE + ": " + message, cause);
    }

    public InvalidRequest(Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HTTP_STATUS;
    }
}
