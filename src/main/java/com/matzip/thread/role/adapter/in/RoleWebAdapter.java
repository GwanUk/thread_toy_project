package com.matzip.thread.role.adapter.in;

import com.matzip.thread.common.annotation.WebAdapter;
import com.matzip.thread.role.application.prot.in.RoleWebPort;
import com.matzip.thread.role.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@WebAdapter(path = "/api/role")
class RoleWebAdapter {

    private final RoleWebPort roleWebPort;

    @GetMapping("/{role}")
    Optional<RoleResponse> findByRole(@PathVariable Role role) {
        return roleWebPort.findByRole(role).map(RoleResponse::toResponse);
    }

    @GetMapping
    List<RoleResponse> findAll() {
        return roleWebPort.findAll().stream().map(RoleResponse::toResponse).toList();
    }

    @PostMapping
    void save(@RequestBody @Validated RoleSave roleSave) {
        roleWebPort.save(roleSave.toEntity());
    }

    @PutMapping("/{role}")
    void update(@PathVariable Role role, @RequestBody @Validated RoleUpdate roleUpdate) {
        roleWebPort.update(role, roleUpdate.toEntity());
    }

    @DeleteMapping("/{role}")
    void delete(@PathVariable Role role) {
        roleWebPort.delete(role);
    }
}
