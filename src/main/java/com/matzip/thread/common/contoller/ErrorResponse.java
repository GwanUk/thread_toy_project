package com.matzip.thread.common.contoller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String message;
    private final List<FieldError> fieldErrors = new ArrayList<>();

    public void addFieldError(org.springframework.validation.FieldError fieldError) {
        fieldErrors.add(new FieldError(fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue()));
    }

    public void addFieldError(String field, String errorMessage, Object rejectedValue) {
        fieldErrors.add(new FieldError(field, errorMessage, rejectedValue));
    }

    @Getter
    @RequiredArgsConstructor
    static class FieldError {
        private final String field;
        private final String errorMessage;
        private final Object rejectedValue;
    }
}
