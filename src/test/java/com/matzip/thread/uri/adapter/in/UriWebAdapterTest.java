package com.matzip.thread.uri.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.thread.uri.application.port.in.UriInPort;
import com.matzip.thread.uri.domain.UriEntity;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        String json = objectMapper.writeValueAsString(new UriFindRequest("/api/uri/**"));

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
        String json = objectMapper.writeValueAsString(new UriFindRequest(""));

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
        String json = objectMapper.writeValueAsString(new UriFindRequest(null));

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


//    @Test
//    @DisplayName("URI 저장 요청 성공")
//    void save_uri() throws Exception {
//        // given
//        String json = objectMapper.writeValueAsString(new UriSaveRequest("/api/admin/**", 1, List.of()));
//
//        // when
//        mockMvc.perform(post("/api/uri")
//                        .contentType(APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andDo(print());
//
//        // then
//        ArgumentCaptor<UriEntity> uriEntityArgumentCaptor = ArgumentCaptor.forClass(UriEntity.class);
//        then(uriInPort).should(Mockito.times(1)).save(uriEntityArgumentCaptor.capture());
//        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getUri()).isEqualTo("/api/admin/**");
//        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getOrder()).isEqualTo(1);
//        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getRoles()).isEmpty();
//    }
//
//    @Test
//    @DisplayName("URI with Role 저장 요청 성공")
//    void save_uri_role() throws Exception {
//        // given
//        String json = objectMapper.writeValueAsString(new UriSaveRequest("/api/admin/**", 1, List.of(ROLE_USER)));
//
//        // when
//        mockMvc.perform(post("/api/uri")
//                .contentType(APPLICATION_JSON)
//                .content(json))
//                .andExpect(status().isOk())
//                .andDo(print());
//
//        // then
//        ArgumentCaptor<UriEntity> uriEntityArgumentCaptor = ArgumentCaptor.forClass(UriEntity.class);
//        then(uriInPort).should(Mockito.times(1)).save(uriEntityArgumentCaptor.capture());
//        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getUri()).isEqualTo("/api/admin/**");
//        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getOrder()).isEqualTo(1);
//        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getRoles().get(0)).isEqualTo(ROLE_USER);
//    }
}