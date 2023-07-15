package com.matzip.thread.security.filter;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class SecurityTestController {

        @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
        String user() {
            return "user";
        }

        @GetMapping(value = "/vip", produces = MediaType.APPLICATION_JSON_VALUE)
        String vip() {
            return "vip";
        }

        @GetMapping(value = "/manager", produces = MediaType.APPLICATION_JSON_VALUE)
        String manager() {
            return "manager";
        }

        @GetMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
        String admin() {
            return "admin";
        }
    }