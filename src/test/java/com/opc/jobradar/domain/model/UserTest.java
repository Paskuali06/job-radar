package com.opc.jobradar.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void shouldCreateUserWithExpectedData() {
        User user = new User();

        user.setId(1L);
        user.setName("Jaime");
        user.setEmail("jaime@email.com");
        user.setPassword("password");
        user.setRole(UserRole.USER);

        assertEquals(1L, user.getId());
        assertEquals("Jaime", user.getName());
        assertEquals("jaime@email.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(UserRole.USER, user.getRole());
    }

    @Test
    void shouldCreateUserWithUserRole() {
        User user = new User();

        user.setRole(UserRole.USER);

        assertEquals(UserRole.USER, user.getRole());
    }

    @Test
    void shouldCreateUserWithAdminRole() {
        User user = new User();

        user.setRole(UserRole.ADMIN);

        assertEquals(UserRole.ADMIN, user.getRole());
    }
}