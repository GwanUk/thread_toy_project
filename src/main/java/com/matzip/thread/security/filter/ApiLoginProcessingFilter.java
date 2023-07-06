package com.matzip.thread.users.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.thread.users.security.token.ApiAuthenticationToken;
import com.matzip.thread.users.domain.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * POST, application/json 요청 인증처리를 담당하는 필터
 */
public class ApiLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApiLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/api/users/sing_in"));
    }

    /**
     * application/json 요청이고 유저 아이디와 비밀번호 존재할 경우 인증 매니저를 통해 인증한 결과를 돌려줌
     * @param request from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     * redirect as part of a multi-stage authentication process (such as OpenID).
     * @return Authentication
     * @throws AuthenticationException spring security RuntimeException
     * @throws IOException io Exception
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

        if (!isApiRequest(request)) {
            throw new IllegalStateException("Authentication is not supported");
        }


        User user = objectMapper.readValue(request.getReader(), User.class);
        if (!(StringUtils.hasText(user.getUsername()) && StringUtils.hasText(user.getPassword()))) {
            throw new IllegalArgumentException("username or Password is required value");
        }

        ApiAuthenticationToken authenticationToken = new ApiAuthenticationToken(user.getUsername(), user.getPassword());

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    private boolean isApiRequest(HttpServletRequest request) {
        return Objects.equals(request.getMethod(), HttpMethod.POST.toString())
                && Objects.equals(request.getContentType(), MediaType.APPLICATION_JSON_VALUE);
    }
}
