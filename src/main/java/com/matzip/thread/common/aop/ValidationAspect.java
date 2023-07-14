package com.matzip.thread.common.aop;

import com.matzip.thread.common.annotation.NullCheck;
import com.matzip.thread.common.exception.NullArgumentException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Aspect
@Component
public class ValidationAspect {

    @Before("@annotation(com.matzip.thread.common.annotation.Validation)")
    public void validate(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = methodSignature.getMethod().getParameters();

        for (int i = 0; i < args.length; i++) {
            Parameter parameter = parameters[i];
            Object arg = args[i];

            NullCheck annotation = parameter.getAnnotation(NullCheck.class);
            if (nonNull(annotation) && isNull(arg)) {

                log.info("[" + methodSignature + "] " + parameter.getType().toString() + " is null");
                throw new NullArgumentException(parameter.getType().getSimpleName());
            }
        }
    }
}
