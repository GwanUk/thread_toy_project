package com.matzip.thread.common.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {

    private final HttpStatus code;
    private final String message;
    private final List<FieldError> fieldErrors = new ArrayList<>();

    public ErrorResponse(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

    public void addFieldError(org.springframework.validation.FieldError fieldError) {
        fieldErrors.add(new FieldError(fieldError.getField(), fieldError.getDefaultMessage()));
    }

    @Getter
    @RequiredArgsConstructor
    static class FieldError {
        private final String field;
        private final String error;
    }
}
