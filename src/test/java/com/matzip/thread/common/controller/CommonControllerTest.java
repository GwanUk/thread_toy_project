package com.matzip.thread.common.controller;

import com.matzip.thread.users.UserControllerTestContextConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = CommonController.class)
@Import(UserControllerTestContextConfiguration.class)
class CommonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("홈 화면 조회 성공")
    void getHome() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Home 화면 입니다."))
                .andDo(MockMvcResultHandlers.print());
    }
}