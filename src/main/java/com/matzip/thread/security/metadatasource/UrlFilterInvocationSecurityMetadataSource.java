package com.matzip.thread.security.metadatasource;

import com.matzip.thread.common.Repository.ResourcesJpaRepository;
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

    private final ResourcesJpaRepository resourcesJpaRepository;

    private final Map<RequestMatcher, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();

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

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        renewRequestMap();
    }

    private void renewRequestMap() {
        requestMap.clear();
        resourcesJpaRepository.findAllWithRoles().forEach(r -> {
            List<ConfigAttribute> list = new ArrayList<>();
            r.getResourcesRoles().forEach(rr -> list.add(new SecurityConfig(rr.getRolesJpaEntity().getRoleName())));
            requestMap.put(new AntPathRequestMatcher(r.getUri()), list);
        });
    }
}
