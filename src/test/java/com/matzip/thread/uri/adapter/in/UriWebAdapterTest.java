package com.matzip.thread.uri.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.thread.uri.application.port.in.UriInPort;
import com.matzip.thread.uri.domain.UriEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.matzip.thread.role.domain.Role.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("uri 전체 조회")
    void findAll() throws Exception {
        // given
        given(uriInPort.findAll()).willReturn(List.of(
                new UriEntity("/api/uri/**", 1,  List.of(ROLE_USER, ROLE_VIP)),
                new UriEntity("/api/**", 2, List.of(ROLE_MANAGER, ROLE_ADMIN))));

        // when
        mockMvc.perform(get("/api/uri/all")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uri").value("/api/uri/**"))
                .andExpect(jsonPath("$[0].order").value(1))
                .andExpect(jsonPath("$[0].roles[0]").value(ROLE_USER.name()))
                .andExpect(jsonPath("$[0].roles[1]").value(ROLE_VIP.name()))
                .andExpect(jsonPath("$[1].uri").value("/api/**"))
                .andExpect(jsonPath("$[1].order").value(2))
                .andExpect(jsonPath("$[1].roles[0]").value(ROLE_MANAGER.name()))
                .andExpect(jsonPath("$[1].roles[1]").value(ROLE_ADMIN.name()))
                .andDo(print());

        // then
        then(uriInPort).should(times(1)).findAll();
    }

    @Test
    @DisplayName("uri 단건 조회")
    void findByUri() throws Exception {
        // given
        Optional<UriEntity> entityOptional = Optional.of(new UriEntity("/api/uri/**", 1, List.of(ROLE_USER, ROLE_VIP)));
        given(uriInPort.findByUri(anyString())).willReturn(entityOptional);

        String json = objectMapper.writeValueAsString(new UriRequest("/api/uri/**"));

        // when
        mockMvc.perform(get("/api/uri")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uri").value("/api/uri/**"))
                .andExpect(jsonPath("$.order").value(1))
                .andExpect(jsonPath("$.roles[0]").value(ROLE_USER.name()))
                .andExpect(jsonPath("$.roles[1]").value(ROLE_VIP.name()))
                .andDo(print());

        // then
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        then(uriInPort).should(times(1)).findByUri(ac.capture());
        assertThat(ac.getValue()).isEqualTo("/api/uri/**");
    }

    @Test
    @DisplayName("uri 단건 빈 문자열 조회")
    void findByUri_empty() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new UriRequest(""));

        // expected
        mockMvc.perform(get("/api/uri")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid argument value"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("uri"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("must not be blank"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("uri 단건 널 조회")
    void findByUri_null() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new UriRequest(null));

        // expected
        mockMvc.perform(get("/api/uri")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid argument value"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("uri"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("must not be blank"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("body 에 null 전송")
    void findByUri_body_null() throws Exception {
        // expected
        mockMvc.perform(get("/api/uri")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Http message not readable"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("body 에 빈 문자열 전송")
    void findByUri_body_empty() throws Exception {
        // expected
        mockMvc.perform(get("/api/uri")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Http message not readable"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty())
                .andDo(print());
    }


    @Test
    @DisplayName("URI 저장")
    void save_uri() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new UriSaveRequest("/api/uri/**", 1, List.of(ROLE_ADMIN, ROLE_MANAGER)));

        // when
        mockMvc.perform(post("/api/uri")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        ArgumentCaptor<UriEntity> ac = ArgumentCaptor.forClass(UriEntity.class);
        then(uriInPort).should(Mockito.times(1)).save(ac.capture());
        BDDAssertions.then(ac.getValue().getUri()).isEqualTo("/api/uri/**");
        BDDAssertions.then(ac.getValue().getOrder()).isEqualTo(1);
        BDDAssertions.then(ac.getValue().getRoles().get(0)).isEqualTo(ROLE_ADMIN);
        BDDAssertions.then(ac.getValue().getRoles().get(1)).isEqualTo(ROLE_MANAGER);
    }

    @Test
    @DisplayName("URI 빈 문자열 저장 벨리데이션 확인")
    void save_uri_empty_validation_check() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new UriSaveRequest("", 1, List.of(ROLE_ADMIN, ROLE_MANAGER)));

        // expected
        mockMvc.perform(post("/api/uri")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid argument value"))
                .andDo(print());
    }

    @Test
    @DisplayName("URI 공백 문자열 저장 벨리데이션 확인")
    void save_uri_blank_validation_check() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new UriSaveRequest(" ", 1, List.of(ROLE_ADMIN, ROLE_MANAGER)));

        // expected
        mockMvc.perform(post("/api/uri")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid argument value"))
                .andDo(print());
    }

    @Test
    @DisplayName("삭제")
    void delete_uri() throws Exception {
        String json = objectMapper.writeValueAsString(new UriRequest("/api/uri"));

        // expected
        mockMvc.perform(delete("/api/uri")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        then(uriInPort).should(times(1)).delete(ac.capture());
        assertThat(ac.getValue()).isEqualTo("/api/uri");
    }

    @Test
    @DisplayName("uri 단건 널 조회")
    void delete_uri_null() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new UriRequest(null));

        // expected
        mockMvc.perform(delete("/api/uri")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid argument value"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("uri"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("must not be blank"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").isEmpty())
                .andDo(print());
    }
}