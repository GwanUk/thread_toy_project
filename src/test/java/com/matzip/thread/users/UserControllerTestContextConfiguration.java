package com.matzip.thread.users;

import com.matzip.thread.security.configs.SecurityConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(SecurityConfig.class)
public class UserControllerTestContextConfiguration {
}
