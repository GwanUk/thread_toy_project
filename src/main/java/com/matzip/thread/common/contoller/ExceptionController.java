package com.matzip.thread.common.contoller;

import com.matzip.thread.common.exception.ApplicationException;
import com.matzip.thread.common.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException exception) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.toString());
        exception.getFieldErrors().iterator().forEachRemaining(response::addFieldError);
        return response;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> applicationException(ApplicationException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ErrorResponse(exception.getMessage()));
    }
}
