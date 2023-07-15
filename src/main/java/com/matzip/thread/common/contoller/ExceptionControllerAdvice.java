package com.matzip.thread.common.contoller;

import com.matzip.thread.common.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse httpMessageNotReadableException(HttpMessageNotReadableException exception) {
        ErrorResponse errorResponse = new ErrorResponse("Http message not readable");
        log.error("Http message not readable", exception);
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        ErrorResponse response = new ErrorResponse("Failed to convert value");
        response.addFieldError(exception.getName(), exception.getErrorCode(), exception.getValue());
        log.error("Failed to convert value", exception);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException exception) {
        ErrorResponse response = new ErrorResponse("Invalid argument value");
        exception.getFieldErrors().iterator().forEachRemaining(response::addFieldError);
        log.error("Invalid argument value", exception);
        return response;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> applicationException(ApplicationException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ErrorResponse(exception.getClass().getSimpleName() + ": " + exception.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse notHandledException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse("Invalid request");
        log.error("Unhandled exception", exception);
        return errorResponse;
    }
}
