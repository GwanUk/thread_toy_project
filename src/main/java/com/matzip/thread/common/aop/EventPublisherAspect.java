package com.matzip.thread.common.aop;

import com.matzip.thread.common.annotation.PublishEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class EventPublisherAspect {

    private final ApplicationEventPublisher applicationEventPublisher;

    @AfterReturning(value = "@annotation(com.matzip.thread.common.annotation.PublishEvent) && @annotation(annotation)", returning = "result")
    public void eventPublish(JoinPoint joinPoint, PublishEvent annotation, Object result) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.info("start [{}::{}] {}", LocalDateTime.now(), Thread.currentThread().getId(), joinPoint.getSignature());

        Object object = annotation.value()
                .getDeclaredConstructor()
                .newInstance();

        log.info("annotation = {}, object = {}", annotation, object);

        applicationEventPublisher.publishEvent(object);

        log.info("finished [{}::{}] {}", LocalDateTime.now(), Thread.currentThread().getId(), joinPoint.getSignature());
    }
}
