package com.matzip.thread.common.exception;

import org.springframework.http.HttpStatus;

public abstract class ApplicationException extends RuntimeException{
    public ApplicationException() {
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public abstract HttpStatus getHttpStatus();

    public abstract String getErrorMessage();
}
