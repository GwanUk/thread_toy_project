package com.matzip.thread.role.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.thread.role.application.prot.in.RoleInPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = RoleWebAdapter.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class RoleWebAdapterTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RoleInPort roleInPort;

    @Test
    @DisplayName("ROLE_USER 저장 성공")
    void save() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new RoleSaveRequest(Role.ROLE_USER, "유저 권한"));

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
        ArgumentCaptor<RoleEntity> roleEntityArgumentCaptor = ArgumentCaptor.forClass(RoleEntity.class);
        BDDMockito.then(roleInPort).should(Mockito.times(1)).save(roleEntityArgumentCaptor.capture());
        BDDAssertions.then(roleEntityArgumentCaptor.getValue().getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(roleEntityArgumentCaptor.getValue().getDescription()).isEqualTo("유저 권한");
    }

}