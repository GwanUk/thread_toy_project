package com.matzip.thread.common.aop;

import com.matzip.thread.common.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Order(1)
@Component
@RequiredArgsConstructor
public class ArgumentValidatorAspect {

    @Before("execution(* com.matzip.thread..*(..)) && @target(com.matzip.thread.common.annotation.ArgumentValidation)")
    public void argumentValidate(JoinPoint joinPoint) {
        log.info(joinPoint.getSignature().toShortString());
        Object[] args = joinPoint.getArgs();
        Arrays.stream(args)
                .filter(Validator.class::isInstance)
                .map(Validator.class::cast)
                .forEach(Validator::validate);
    }
}
