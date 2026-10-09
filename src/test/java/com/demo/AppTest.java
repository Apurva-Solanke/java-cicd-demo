package com.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {
    @Test
    void homeReturnsMessage() {
        assertEquals("Hello from Jenkins CI/CD pipeline! v2", new App().home());
    }
}
