package com.opc.jobradar.application.service;

import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.domain.model.UserRole;
import com.opc.jobradar.domain.port.out.PasswordHasher;
import com.opc.jobradar.domain.port.out.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class RegisterUserServiceTest {

    @Test
    void shouldRegisterUser() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordHasher passwordHasher = mock(PasswordHasher.class);

        when(userRepository.existsByEmail("jaime@email.com"))
                .thenReturn(false);

        when(passwordHasher.hash("password"))
                .thenReturn("hashed-password");

        RegisterUserService service =
                new RegisterUserService(userRepository, passwordHasher);

        service.register(
                "Jaime",
                "jaime@email.com",
                "password"
        );

        verify(userRepository).save(argThat(user ->
                user.getName().equals("Jaime")
                        && user.getEmail().equals("jaime@email.com")
                        && user.getPassword().equals("hashed-password")
                        && user.getRole() == UserRole.USER
        ));
    }

    @Test
    void shouldRejectDuplicatedEmail() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordHasher passwordHasher = mock(PasswordHasher.class);

        when(userRepository.existsByEmail("jaime@email.com"))
                .thenReturn(true);

        RegisterUserService service =
                new RegisterUserService(userRepository, passwordHasher);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.register(
                        "Otro",
                        "jaime@email.com",
                        "password"
                )
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldHashPasswordBeforeSavingUser() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordHasher passwordHasher = mock(PasswordHasher.class);

        when(userRepository.existsByEmail("jaime@email.com"))
                .thenReturn(false);

        when(passwordHasher.hash("password"))
                .thenReturn("hashed-password");

        RegisterUserService service =
                new RegisterUserService(userRepository, passwordHasher);

        service.register(
                "Jaime",
                "jaime@email.com",
                "password"
        );

        verify(userRepository).save(argThat(user ->
                user.getPassword().equals("hashed-password")
        ));
    }
}