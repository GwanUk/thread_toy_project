package com.matzip.thread.uri.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import com.matzip.thread.uri.application.port.in.UriInPort;
import com.matzip.thread.uri.domain.UriEntity;
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

@WebMvcTest(controllers = UriWebAdapter.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UriWebAdapterTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UriInPort uriInPort;

    @Test
    @DisplayName("uri 자원 전체 조회 성공")
    void findAll() throws Exception {
        // given
        BDDMockito.given(uriInPort.findAll()).willReturn(List.of(
                new UriEntity("/api/user/**", 1,  List.of(Role.ROLE_USER)),
                new UriEntity("/api/admin/**", 2, List.of(Role.ROLE_ADMIN))));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/uri")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].uriName").value("/api/user/**"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].uriOrder").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].roles[0]").value(Role.ROLE_USER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].uriName").value("/api/admin/**"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].uriOrder").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].roles[0]").value(Role.ROLE_ADMIN.name()))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    @DisplayName("URI 저장 요청 성공")
    void save_uri() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new UriSaveRequest("/api/admin/**", 1, List.of()));

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/uri")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
        ArgumentCaptor<UriEntity> uriEntityArgumentCaptor = ArgumentCaptor.forClass(UriEntity.class);
        BDDMockito.then(uriInPort).should(Mockito.times(1)).save(uriEntityArgumentCaptor.capture());
        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getUriName()).isEqualTo("/api/admin/**");
        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getUriOrder()).isEqualTo(1);
        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getRoles()).isEmpty();
    }

    @Test
    @DisplayName("URI with Role 저장 요청 성공")
    void save_uri_role() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new UriSaveRequest("/api/admin/**", 1, List.of(Role.ROLE_USER)));

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/uri")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
        ArgumentCaptor<UriEntity> uriEntityArgumentCaptor = ArgumentCaptor.forClass(UriEntity.class);
        BDDMockito.then(uriInPort).should(Mockito.times(1)).save(uriEntityArgumentCaptor.capture());
        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getUriName()).isEqualTo("/api/admin/**");
        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getUriOrder()).isEqualTo(1);
        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getRoles().get(0)).isEqualTo(Role.ROLE_USER);
    }
}