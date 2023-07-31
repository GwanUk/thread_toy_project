package com.matzip.thread.user.adapter.in;

import com.matzip.thread.common.annotation.WebAdapter;
import com.matzip.thread.user.application.port.in.UserWebPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@WebAdapter(path = "/api/users")
@RequiredArgsConstructor
class UserWebAdapter {

    private final UserWebPort userWebPort;

    private final PasswordEncoder passwordEncoder;

    @GetMapping
    List<UserResponse> findAll() {
        return userWebPort.findAll().stream().map(UserResponse::from).toList();
    }

    @GetMapping("/{username}")
    Optional<UserResponse> findByUser(@PathVariable String username) {
        return userWebPort.findByUsername(username).map(UserResponse::from);
    }

    @PostMapping("/sign_up")
    void signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        userWebPort.signUp(signUpRequest.toEntity(passwordEncoder));
    }
}
