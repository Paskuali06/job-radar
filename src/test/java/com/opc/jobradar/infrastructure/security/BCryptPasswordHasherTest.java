package com.opc.jobradar.infrastructure.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BCryptPasswordHasherTest {

    @Test
    void shouldHashPassword() {
        BCryptPasswordHasher passwordHasher = new BCryptPasswordHasher();

        String password = "password";

        String hashedPassword = passwordHasher.hash(password);

        assertNotEquals(password, hashedPassword);
        assertTrue(hashedPassword.startsWith("$2"));
    }
}