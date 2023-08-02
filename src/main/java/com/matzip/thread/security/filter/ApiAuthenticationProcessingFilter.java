package com.matzip.thread.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.thread.security.token.ApiAuthenticationToken;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * POST, application/json 요청 인증처리를 담당하는 필터
 */
public class ApiAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    protected AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    private final ObjectMapper objectMapper;

    public ApiAuthenticationProcessingFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/api/users/sing_in"));
        this.objectMapper = objectMapper;
    }

    /**
     * application/json 요청이고 유저 아이디와 비밀번호 존재할 경우 인증, 성공시 인증토큰 리턴
     * @param request from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     * redirect as part of a multi-stage authentication process (such as OpenID).
     * @return ApiAuthenticationToken
     * @throws AuthenticationException spring security RuntimeException
     * @throws IOException io Exception
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        if (!isPostAndJson(request)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().print("Supports only POST and application/json");
            return null;
        }

        SignInRequest signInRequest = objectMapper.readValue(request.getReader(), SignInRequest.class);
        if (!signInRequest.validate()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().print("non-existent user or a wrong password");
            return null;
        }

        ApiAuthenticationToken authRequest = new ApiAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword());
        setDetails(request, authRequest);
        return getAuthenticationManager().authenticate(authRequest);
    }

    private boolean isPostAndJson(HttpServletRequest request) {
        return Objects.equals(request.getMethod(), HttpMethod.POST.toString())
                && Objects.equals(request.getContentType(), MediaType.APPLICATION_JSON_VALUE);
    }

    protected void setDetails(HttpServletRequest request, ApiAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
