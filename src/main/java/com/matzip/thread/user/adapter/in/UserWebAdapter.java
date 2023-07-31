package com.matzip.thread.user.adapter.in;

import com.matzip.thread.common.annotation.WebAdapter;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.user.application.port.in.UserWebPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@WebAdapter(path = "/api/users")
@RequiredArgsConstructor
class UserWebAdapter {

    private final UserWebPort userWebPort;

    private final PasswordEncoder passwordEncoder;

    @GetMapping
    List<UserResponse> findAll() {
        return userWebPort.findAll().stream().map(UserResponse::from).toList();
    }

    @PostMapping("/sign_up")
    void signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        userWebPort.signUp(signUpRequest.toEntity(passwordEncoder), Role.ROLE_USER);
    }
}
