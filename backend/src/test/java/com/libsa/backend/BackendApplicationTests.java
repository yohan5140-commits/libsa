package com.libsa.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "data4library.auth-key=test-key")
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }
}
