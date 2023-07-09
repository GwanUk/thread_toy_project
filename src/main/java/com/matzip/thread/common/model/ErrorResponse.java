package com.matzip.thread.common.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

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

    @Getter
    @RequiredArgsConstructor
    static class FieldError {
        private final String field;
        private final String errorMessage;
        @Nullable
        private final Object rejectedValue;
    }
}
