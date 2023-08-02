package com.matzip.thread.role.application.event;

import com.matzip.thread.role.application.prot.in.RoleSecurityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@RequiredArgsConstructor
public class RoleChangedEventHandler {

    private final RoleHierarchyImpl roleHierarchy;
    private final RoleSecurityPort roleSecurityPort;

    @EventListener
    public void loadRoleHierarchy(RoleChangedEvent roleChangedEvent) {
        roleHierarchy.setHierarchy(roleSecurityPort.getHierarchy());
    }
}
