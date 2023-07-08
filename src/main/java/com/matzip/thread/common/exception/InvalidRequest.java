package com.matzip.thread.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequest extends ApplicationException{

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    private static final String ERROR_MESSAGE = "request is invalid";

    private String message;

    public InvalidRequest() {
    }

    public InvalidRequest(String message) {
        this.message = message;
    }

    public InvalidRequest(Throwable cause) {
        super(cause);
    }

    public InvalidRequest(Throwable cause, String message) {
        super(cause);
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HTTP_STATUS;
    }

    @Override
    public String getErrorMessage() {
        if (message == null || message.equals("") || message.equals(" ")) {
            return ERROR_MESSAGE;
        }
        return ERROR_MESSAGE + ": " + message;
    }
}
