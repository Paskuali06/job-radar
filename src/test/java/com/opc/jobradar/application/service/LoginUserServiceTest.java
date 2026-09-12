package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.InvalidCredentialsException;
import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.domain.model.UserRole;
import com.opc.jobradar.domain.port.out.PasswordHasher;
import com.opc.jobradar.domain.port.out.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
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

        User result =
                service.login("jaime@email.com", "password");

        assertEquals(1L, result.getId());
        assertEquals("Jaime", result.getName());
        assertEquals("jaime@email.com", result.getEmail());
        assertEquals(UserRole.USER, result.getRole());
    }

    @Test
    void shouldRejectLoginWhenUserDoesNotExist() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordHasher passwordHasher = mock(PasswordHasher.class);

        when(userRepository.findByEmail("unknown@email.com"))
                .thenReturn(null);

        LoginUserService service =
                new LoginUserService(userRepository, passwordHasher);

        assertThrows(
                InvalidCredentialsException.class,
                () -> service.login(
                        "unknown@email.com",
                        "password"
                )
        );

        verify(passwordHasher, never())
                .matches(anyString(), anyString());
    }

    @Test
    void shouldRejectLoginWhenPasswordIsIncorrect() {
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

        when(passwordHasher.matches("wrong-password", "hashed-password"))
                .thenReturn(false);

        LoginUserService service =
                new LoginUserService(userRepository, passwordHasher);

        assertThrows(
                InvalidCredentialsException.class,
                () -> service.login(
                        "jaime@email.com",
                        "wrong-password"
                )
        );
    }
}