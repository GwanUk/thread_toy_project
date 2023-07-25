package com.matzip.thread.common.aop;

import com.matzip.thread.common.annotation.Retry;
import com.matzip.thread.common.exception.ApplicationException;
import com.matzip.thread.common.exception.UpdateFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Order(2)
@Component
@RequiredArgsConstructor
public class RetryAspect {

    @Around("@annotation(retry)")
    public Object retry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {

        int maxCnt = retry.value();
        ApplicationException exceptionHolder = null;

        for (int i = 0; i < maxCnt; i++) {
            try {
                return joinPoint.proceed();
            } catch (UpdateFailureException exception) {
                exceptionHolder = exception;
            }
        }

        throw exceptionHolder;
    }
}
