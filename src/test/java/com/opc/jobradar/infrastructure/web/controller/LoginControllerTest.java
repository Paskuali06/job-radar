package com.opc.jobradar.infrastructure.web.controller;

import com.opc.jobradar.application.exception.InvalidCredentialsException;
import com.opc.jobradar.application.service.LoginUserService;
import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.domain.model.UserRole;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    private final LoginUserService loginUserService =
            mock(LoginUserService.class);

    private final LoginController controller =
            new LoginController(loginUserService);

    @Test
    void shouldEstablishUserIdentityInSessionWhenCredentialsAreValid() {
        User user = new User();
        user.setId(1L);
        user.setName("Jaime");
        user.setEmail("jaime@test.com");
        user.setRole(UserRole.USER);

        HttpSession session = mock(HttpSession.class);

        when(loginUserService.login("jaime@test.com", "password"))
                .thenReturn(user);

        controller.login("jaime@test.com", "password", session);

        verify(session).setAttribute("USER_ID", 1L);
    }

    @Test
    void shouldNotEstablishUserIdentityWhenCredentialsAreInvalid() {
        HttpSession session = mock(HttpSession.class);

        when(loginUserService.login("jaime@test.com", "wrong"))
                .thenThrow(new InvalidCredentialsException());

        assertThrows(
                InvalidCredentialsException.class,
                () -> controller.login(
                        "jaime@test.com",
                        "wrong",
                        session
                )
        );

        verify(session, never())
                .setAttribute(anyString(), any());
    }

    @Test
    void shouldInvalidateSessionWhenLogout() {
        HttpSession session = mock(HttpSession.class);

        controller.logout(session);

        verify(session).invalidate();
    }
}