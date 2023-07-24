package com.matzip.thread.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 낙관적 락 재시도를 위한 예외
 */
public class UpdateFailureException extends ApplicationException {
    private static final String msg = "Update failed. Please try again in a few minutes";

    public UpdateFailureException() {
        super(msg);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.REQUEST_TIMEOUT;
    }
}
