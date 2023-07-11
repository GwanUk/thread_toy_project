package com.matzip.thread.role.adapter.in;

import com.matzip.thread.common.annotation.WebAdapter;
import com.matzip.thread.role.application.prot.in.RoleInPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@WebAdapter(path = "/api/role")
class RoleWebAdapter {

    private final RoleInPort roleInPort;

    @PostMapping
    void save(@RequestBody RoleSaveRequest roleSaveRequest) {
        roleInPort.save(roleSaveRequest.toEntity());
    }
}
