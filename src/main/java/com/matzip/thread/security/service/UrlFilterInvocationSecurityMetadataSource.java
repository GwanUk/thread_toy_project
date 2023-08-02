package com.matzip.thread.security.service;

import com.matzip.thread.uri.application.event.UriChangedEvent;
import com.matzip.thread.uri.application.port.in.UriSecurityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.*;

@RequiredArgsConstructor
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final UriSecurityPort uriSecurityPort;

    private final Map<RequestMatcher, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        loadRequestMap();
    }

    @EventListener
    public void reload(UriChangedEvent uriChangedEvent) {
        requestMap.clear();
        loadRequestMap();
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        return requestMap.entrySet()
                .stream()
                .filter(e -> e.getKey().matches(((FilterInvocation) object).getRequest()))
                .map(Map.Entry::getValue)
                .findAny()
                .orElse(null);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();
        requestMap.values().forEach(allAttributes::addAll);
        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    private void loadRequestMap() {
        uriSecurityPort.findAll().forEach(u -> requestMap.put(
                new AntPathRequestMatcher(u.getUri()),
                u.getRoles().stream()
                        .<ConfigAttribute>map(r -> new SecurityConfig(r.name()))
                        .toList()
                ));
    }
}
