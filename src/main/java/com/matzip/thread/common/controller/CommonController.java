package com.matzip.thread.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
public class CommonController {

    @GetMapping("/")
    public String home() {
        return "Home 화면 입니다.";
    }

    @GetMapping("/test")
    public String test() {
        return "success test!";
    }


}
