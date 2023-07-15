package com.matzip.thread.role.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.thread.role.application.prot.in.RoleWebPort;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(controllers = RoleWebAdapter.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class RoleWebAdapterTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RoleWebPort roleWebPort;

    @Test
    @DisplayName("단건 null 조회 요청")
    void findByRole_null() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/role/{role}", "null")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Failed to convert value"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("role"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].errorMessage").value("typeMismatch"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].rejectedValue").value("null"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("role 아닌 값 조회 요청")
    void findByRole_incorrect_value() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/role/{role}", "yes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Failed to convert value"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("role"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].errorMessage").value("typeMismatch"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].rejectedValue").value("yes"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("단건 조회 요청")
    void findByRole() throws Exception {
        // given
        BDDMockito.given(roleWebPort.findByRole(Mockito.any())).willReturn(new RoleEntity(Role.ROLE_USER, "유저 권한", null, List.of()));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/role/{role}", Role.ROLE_USER)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(Role.ROLE_USER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("유저 권한"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.parent").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.children").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("전체 조회 성공")
    void findAll() throws Exception {
        // given
        BDDMockito.given(roleWebPort.findAll()).willReturn(List.of(
                new RoleEntity(Role.ROLE_USER, "유저 권한", null, List.of()),
                new RoleEntity(Role.ROLE_VIP, "특별 권한", null, List.of()),
                new RoleEntity(Role.ROLE_MANAGER, "매니저 권한", null, List.of()),
                new RoleEntity(Role.ROLE_ADMIN, "관리자 권한", null, List.of())
        ));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/role")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].role").value(Role.ROLE_USER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("유저 권한"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].parent").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].children").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].role").value(Role.ROLE_VIP.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("특별 권한"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].parent").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].children").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].role").value(Role.ROLE_MANAGER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].description").value("매니저 권한"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].parent").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].children").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].role").value(Role.ROLE_ADMIN.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].description").value("관리자 권한"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].parent").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].children").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("null 저장 요청")
    void save_null() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Http message not readable"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("role-null 값 저장 요청")
    void save_role_null() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new RoleSaveRequest(null, "유저 권한", null, List.of()));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid argument value"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("role"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].errorMessage").value("must not be null"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].rejectedValue").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("role 아닌 틀린 값 저장 요청")
    void save_incorrect_value() throws Exception {
        // given
        String json = objectMapper.createObjectNode()
                .put("role", "abc")
                .put("description", "유저 권한")
                .put("parent", "null")
                .put("children", "[]")
                .toString();


        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Http message not readable"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("숫자 저장 요청")
    void save_number() throws Exception {
        // given
        String json = objectMapper.createObjectNode()
                .put("role", "123")
                .put("description", "유저 권한")
                .put("parent", "null")
                .put("children", "[]")
                .toString();


        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Http message not readable"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("저장 성공")
    void save() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new RoleSaveRequest(Role.ROLE_USER, "유저 권한", null, List.of()));

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
        ArgumentCaptor<RoleEntity> roleEntityArgumentCaptor = ArgumentCaptor.forClass(RoleEntity.class);
        BDDMockito.then(roleWebPort).should(Mockito.times(1)).save(roleEntityArgumentCaptor.capture());
        BDDAssertions.then(roleEntityArgumentCaptor.getValue().getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(roleEntityArgumentCaptor.getValue().getDescription()).isEqualTo("유저 권한");
        BDDAssertions.then(roleEntityArgumentCaptor.getValue().getParent()).isNull();
        BDDAssertions.then(roleEntityArgumentCaptor.getValue().getChildren()).isEmpty();
    }
}