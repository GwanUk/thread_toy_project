package com.matzip.thread.security.metadatasource;

import com.matzip.thread.uri.application.port.in.UriInPort;
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

    private final UriInPort uriInPort;

    private final Map<RequestMatcher, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        renewRequestMap();
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

    public void reload() {
        renewRequestMap();
    }

    private void renewRequestMap() {
        requestMap.clear();
        uriInPort.findAll().forEach(u -> requestMap.put(
                new AntPathRequestMatcher(u.getUriName()),
                u.getRoles().stream()
                        .<ConfigAttribute>map(r -> new SecurityConfig(r.getRole().name()))
                        .toList()));
    }
}
