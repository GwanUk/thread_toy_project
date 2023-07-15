package com.matzip.thread.role.adapter.in;

import com.matzip.thread.common.annotation.WebAdapter;
import com.matzip.thread.role.application.prot.in.RoleWebPort;
import com.matzip.thread.role.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@WebAdapter(path = "/api/role")
class RoleWebAdapter {

    private final RoleWebPort roleWebPort;

    @GetMapping("/{role}")
    RoleResponse findByRole(@PathVariable Role role) {
        return RoleResponse.toResponse(roleWebPort.findByRole(role));
    }

    @GetMapping
    List<RoleResponse> findAll() {
        return roleWebPort.findAll().stream().map(RoleResponse::toResponse).toList();
    }

    @PostMapping
    void save(@RequestBody @Validated RoleSaveRequest roleSaveRequest) {
        roleWebPort.save(roleSaveRequest.toEntity());
    }
}
