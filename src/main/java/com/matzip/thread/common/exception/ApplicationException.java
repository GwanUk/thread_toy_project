package com.matzip.thread.common.exception;

import org.springframework.http.HttpStatus;

public abstract class ApplicationException extends RuntimeException{

    public ApplicationException() {
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public abstract HttpStatus getHttpStatus();
}
