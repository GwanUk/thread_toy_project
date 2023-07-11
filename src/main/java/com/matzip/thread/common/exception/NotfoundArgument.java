package com.matzip.thread.common.exception;

import org.springframework.http.HttpStatus;

public class NotfoundArgument extends ApplicationException{

    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    private static final String ERROR_MESSAGE = "doesn't exist";

    public NotfoundArgument() {
        super(ERROR_MESSAGE);
    }

    public NotfoundArgument(String message) {
        super(ERROR_MESSAGE + " " + message);
    }

    public NotfoundArgument(String message, Throwable cause) {
        super(ERROR_MESSAGE + " " + message, cause);
    }

    public NotfoundArgument(Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HTTP_STATUS;
    }
}
