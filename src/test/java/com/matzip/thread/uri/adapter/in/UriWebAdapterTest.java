package com.matzip.thread.uri.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    @DisplayName("URI 자원 저장 성공")
    void save() throws Exception {
        // given
        String json = objectMapper.createObjectNode()
                .put("uriName", "/api/admin/**")
                .put("uriOrder", 1)
                .toString();

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
    }
}