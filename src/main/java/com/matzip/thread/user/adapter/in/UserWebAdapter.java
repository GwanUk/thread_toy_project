package com.matzip.thread.user.adapter.in;

import com.matzip.thread.common.annotation.WebAdapter;
import com.matzip.thread.user.application.port.in.UserWebPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    void save(@Validated @RequestBody UserSaveRequest userSaveRequest) {
        userWebPort.save(userSaveRequest.toEntity(passwordEncoder));
    }

    @PutMapping("/{username}")
    void update(@PathVariable String username, @Validated @RequestBody UserUpdateRequest userUpdateRequest) {
        userWebPort.update(username, userUpdateRequest.toEntity(passwordEncoder));
    }

    @DeleteMapping("/{username}")
    void delete(@PathVariable String username) {
        userWebPort.delete(username);
    }
}
