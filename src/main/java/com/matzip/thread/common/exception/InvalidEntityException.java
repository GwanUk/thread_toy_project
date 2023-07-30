package com.matzip.thread.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidEntityException extends ApplicationException{
    private final String clazz;
    private final String field;
    private final Object value;

    public InvalidEntityException(String message, String clazz, String field, Object value) {
        super(message);
        this.clazz = clazz;
        this.field = field;
        this.value = value;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
