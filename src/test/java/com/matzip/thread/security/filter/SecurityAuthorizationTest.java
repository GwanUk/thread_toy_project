package com.matzip.thread.security.filter;

import com.matzip.thread.security.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(SecurityTestController.class)
@Import(SecurityTestConfiguration.class)
public class SecurityAuthorizationTest {

    @Autowired
    private  MockMvc mockMvc;

    @Test
    @DisplayName("요청 실패: 인증 되지 않는 사용자")
    void request_failure_unauthorized() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("UnAuthorized. Only authorized users have access"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("요청 실패: 인가 되지 않은 사용자")
    void request_failure_forbidden() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string("Access denied. Authorization is required"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockCustomUser(username = "admin", role = "ROLE_ADMIN")
    @DisplayName("요청 ROLE_ADMIN 인증 성공")
    void request_success_admin() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("admin"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockCustomUser(username = "vip", role = "ROLE_VIP")
    @DisplayName("권한 계층: VIP 권한을 가지고 /api/user 경로 인증 성공")
    void request_success_user_uri_has_vip() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("user"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockCustomUser(username = "admin", role = "ROLE_ADMIN")
    @DisplayName("권한 계층: ADMIN 권한을 가지고 /api/user 경로 인증 성공")
    void request_success_user_uri_has_admin() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("user"))
                .andDo(MockMvcResultHandlers.print());
    }
}
