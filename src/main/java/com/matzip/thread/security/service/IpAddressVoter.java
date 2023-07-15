package com.matzip.thread.security.service;

import com.matzip.thread.common.exception.securityexception.AccessDeniedIpAddressException;
import com.matzip.thread.ipaddress.application.port.in.IpAddressQueryInPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class IpAddressVoter implements AccessDecisionVoter<Object> {

    private final IpAddressQueryInPort ipAddressQueryInPort;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    /**
     * 차단된 아이피일 경우 예외발생
     * @param authentication ApiAuthenticationToken
     * @param object FilterInvocation has request
     * @param attributes SecurityConfig has Role name
     * @return ACCESS_ABSTAIN
     */
    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String remoteAddress = details.getRemoteAddress();
        List<String> ipAddresses = ipAddressQueryInPort.getIpAddresses();

        if (ipAddresses.stream()
                .noneMatch(remoteAddress::equals)) {

            throw new AccessDeniedIpAddressException(remoteAddress);
        }

        return ACCESS_ABSTAIN;
    }
}
