package com.matzip.thread.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
class CommonController {

    @GetMapping("/")
    String home() {
        return "Home 화면 입니다.";
    }

    @GetMapping("/test")
    String test() {
        return "success test!";
    }


}
