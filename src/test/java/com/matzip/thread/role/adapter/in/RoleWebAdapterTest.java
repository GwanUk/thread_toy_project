package com.matzip.thread.role.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.thread.role.application.prot.in.RoleWebPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.matzip.thread.role.domain.Role.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        mockMvc.perform(get("/api/role/{role}", "null")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Failed to convert value"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("role"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("typeMismatch"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value("null"))
                .andDo(print());
    }

    @Test
    @DisplayName("role 아닌 값 조회 요청")
    void findByRole_incorrect_value() throws Exception {
        // expected
        mockMvc.perform(get("/api/role/{role}", "yes")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Failed to convert value"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("role"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("typeMismatch"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value("yes"))
                .andDo(print());
    }

    @Test
    @DisplayName("단건 조회 요청")
    void findByRole() throws Exception {
        // given
        given(roleWebPort.findByRole(any())).willReturn(Optional.of(new RoleEntity(ROLE_USER, "유저 권한", List.of())));

        // expected
        mockMvc.perform(get("/api/role/{role}", ROLE_USER)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value(ROLE_USER.name()))
                .andExpect(jsonPath("$.description").value("유저 권한"))
                .andExpect(jsonPath("$.parent").isEmpty())
                .andExpect(jsonPath("$.children").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("전체 조회 성공")
    void findAll() throws Exception {
        // given
        given(roleWebPort.findAll()).willReturn(List.of(
                new RoleEntity(ROLE_USER, "유저 권한", List.of()),
                new RoleEntity(ROLE_VIP, "특별 권한", List.of()),
                new RoleEntity(ROLE_MANAGER, "매니저 권한", List.of()),
                new RoleEntity(ROLE_ADMIN, "관리자 권한", List.of())
        ));

        // expected
        mockMvc.perform(get("/api/role")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role").value(ROLE_USER.name()))
                .andExpect(jsonPath("$[0].description").value("유저 권한"))
                .andExpect(jsonPath("$[0].parent").isEmpty())
                .andExpect(jsonPath("$[0].children").isEmpty())
                .andExpect(jsonPath("$[1].role").value(ROLE_VIP.name()))
                .andExpect(jsonPath("$[1].description").value("특별 권한"))
                .andExpect(jsonPath("$[1].parent").isEmpty())
                .andExpect(jsonPath("$[1].children").isEmpty())
                .andExpect(jsonPath("$[2].role").value(ROLE_MANAGER.name()))
                .andExpect(jsonPath("$[2].description").value("매니저 권한"))
                .andExpect(jsonPath("$[2].parent").isEmpty())
                .andExpect(jsonPath("$[2].children").isEmpty())
                .andExpect(jsonPath("$[3].role").value(ROLE_ADMIN.name()))
                .andExpect(jsonPath("$[3].description").value("관리자 권한"))
                .andExpect(jsonPath("$[3].parent").isEmpty())
                .andExpect(jsonPath("$[3].children").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("null 저장 요청")
    void save_null() throws Exception {
        // expected
        mockMvc.perform(post("/api/role")
                        .contentType(APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Http message not readable"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("role-null 값 저장 요청")
    void save_role_null() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new RoleSave(null, "유저 권한", null, List.of()));

        // expected
        mockMvc.perform(post("/api/role")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid argument value"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("role"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("must not be null"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").isEmpty())
                .andDo(print());
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
        mockMvc.perform(post("/api/role")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Http message not readable"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty())
                .andDo(print());
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
        mockMvc.perform(post("/api/role")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Http message not readable"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("저장 성공")
    void save() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new RoleSave(ROLE_VIP, "유저 권한", ROLE_ADMIN, List.of(ROLE_USER)));

        // when
        mockMvc.perform(post("/api/role")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        ArgumentCaptor<RoleEntity> ac = ArgumentCaptor.forClass(RoleEntity.class);
        then(roleWebPort).should(times(1)).save(ac.capture());
        assertThat(ac.getValue().getRole()).isEqualTo(ROLE_VIP);
        assertThat(ac.getValue().getDescription()).isEqualTo("유저 권한");
        assertThat(ac.getValue().getChildren().get(0)).isEqualTo(ROLE_USER);
    }

    @Test
    @DisplayName("null 다른 권한으로 업데이트 시도")
    void update_null_to_role() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new RoleUpdate(ROLE_VIP, "유저 권한", ROLE_ADMIN, List.of(ROLE_USER)));

        // expected
        mockMvc.perform(put("/api/role/{role}", "null")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Failed to convert value"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("role"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("typeMismatch"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value("null"))
                .andDo(print());
    }

    @Test
    @DisplayName("틀린 타입을 다른 권한으로 업데이트 시도")
    void update_incorrect_to_role() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new RoleUpdate(ROLE_VIP, "유저 권한", ROLE_ADMIN, List.of(ROLE_USER)));

        // expected
        mockMvc.perform(put("/api/role/{role}", "abc")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Failed to convert value"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("role"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("typeMismatch"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value("abc"))
                .andDo(print());
    }

    @Test
    @DisplayName("숫자를 다른 권한으로 업데이트 시도")
    void update_number_to_role() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new RoleUpdate(ROLE_VIP, "유저 권한", ROLE_ADMIN, List.of(ROLE_USER)));

        // expected
        mockMvc.perform(put("/api/role/{role}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Failed to convert value"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("role"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("typeMismatch"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value("1"))
                .andDo(print());
    }

    @Test
    @DisplayName("null 로 업데이트 시도")
    void update_to_null() throws Exception {
        // expected
        mockMvc.perform(put("/api/role/{role}", ROLE_USER)
                        .contentType(APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Http message not readable"))
                .andDo(print());
    }

    @Test
    @DisplayName("문자열로 업데이트 시도")
    void update_to_string() throws Exception {
        // given
        String json = objectMapper.createObjectNode()
                .put("role", "null")
                .put("description", "유저 권한")
                .put("parent", "null")
                .put("children", "[]")
                .toString();

        // expected
        mockMvc.perform(put("/api/role/{role}", ROLE_USER)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Http message not readable"))
                .andDo(print());
    }

    @Test
    @DisplayName("업데이트")
    void update() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new RoleUpdate(ROLE_VIP, "유저 권한", ROLE_ADMIN, List.of(ROLE_USER)));

        // when
        mockMvc.perform(put("/api/role/{role}", ROLE_USER)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<RoleEntity> ac = ArgumentCaptor.forClass(RoleEntity.class);
        then(roleWebPort).should(times(1)).update(any(Role.class), ac.capture());
        assertThat(ac.getValue().getRole()).isEqualTo(ROLE_VIP);
        assertThat(ac.getValue().getDescription()).isEqualTo("유저 권한");
        assertThat(ac.getValue().getChildren().get(0)).isEqualTo(ROLE_USER);
    }

    @Test
    @DisplayName("삭제")
    void delete_role() throws Exception {
        // when
        mockMvc.perform(delete("/api/role/{role}", ROLE_USER))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        then(roleWebPort).should(times(1)).delete(any(Role.class));
    }

    @Test
    @DisplayName("null 삭제 시도")
    void delete_role_null() throws Exception {
        // expected
        mockMvc.perform(delete("/api/role/{role}", "null"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Failed to convert value"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("role"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("typeMismatch"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value("null"))
                .andDo(print());
    }

    @Test
    @DisplayName("문자열 삭제 시도")
    void delete_role_string() throws Exception {
        // expected
        mockMvc.perform(delete("/api/role/{role}", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Failed to convert value"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("role"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("typeMismatch"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value("abc"))
                .andDo(print());
    }

    @Test
    @DisplayName("숫자 삭제 시도")
    void delete_role_number() throws Exception {
        // expected
        mockMvc.perform(delete("/api/role/{role}", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Failed to convert value"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("role"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("typeMismatch"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value("1"))
                .andDo(print());
    }
}