package com.matzip.thread.common.exception;

import org.springframework.http.HttpStatus;

public class InfiniteLoopException extends ApplicationException{
    public InfiniteLoopException() {
        super("Circular reference is not possible");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
