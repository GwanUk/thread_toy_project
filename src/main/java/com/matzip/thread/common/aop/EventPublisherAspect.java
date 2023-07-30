package com.matzip.thread.common.aop;

import com.matzip.thread.common.annotation.PublishEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Slf4j
@Aspect
@Order(2)
@Component
@RequiredArgsConstructor
public class EventPublisherAspect {

    private final ApplicationEventPublisher applicationEventPublisher;

    @AfterReturning(value = "@annotation(publishEvent)", returning = "result")
    public void eventPublish(JoinPoint joinPoint, PublishEvent publishEvent, Object result) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object object = publishEvent.value()
                .getDeclaredConstructor()
                .newInstance();

        applicationEventPublisher.publishEvent(object);
    }
}
