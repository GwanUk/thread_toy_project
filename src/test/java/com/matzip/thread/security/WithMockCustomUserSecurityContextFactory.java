package com.matzip.thread.security;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.security.token.ApiAuthenticationToken;
import com.matzip.thread.user.domain.UserEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.List;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        String username = annotation.username();
        String role = annotation.role();
        UserEntity userEntity = new UserEntity(username, null, null, Role.valueOf(role));
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        AbstractAuthenticationToken authenticationToken = new ApiAuthenticationToken(userEntity, null, authorities);
        WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetails("0:0:0:0:0:0:0:1", null);

        authenticationToken.setDetails(webAuthenticationDetails);
        securityContext.setAuthentication(authenticationToken);

        return securityContext;
    }
}