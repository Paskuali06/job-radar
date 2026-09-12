package com.opc.jobradar.application.service;

import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.domain.model.UserRole;
import com.opc.jobradar.domain.port.out.PasswordHasher;
import com.opc.jobradar.domain.port.out.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LoginUserServiceTest {

    @Test
    void shouldLoginWithValidCredentials() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordHasher passwordHasher = mock(PasswordHasher.class);

        User user = new User();
        user.setId(1L);
        user.setName("Jaime");
        user.setEmail("jaime@email.com");
        user.setPassword("hashed-password");
        user.setRole(UserRole.USER);

        when(userRepository.findByEmail("jaime@email.com"))
                .thenReturn(user);

        when(passwordHasher.matches("password", "hashed-password"))
                .thenReturn(true);

        LoginUserService service =
                new LoginUserService(userRepository, passwordHasher);

        boolean result =
                service.login("jaime@email.com", "password");

        assertTrue(result);
    }
}