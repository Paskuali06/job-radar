package com.opc.jobradar.infrastructure.web.controller;

import com.opc.jobradar.application.service.GetAuthenticatedUserService;
import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.domain.model.UserRole;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    private final GetAuthenticatedUserService getAuthenticatedUserService =
            mock(GetAuthenticatedUserService.class);

    private final AccountController controller =
            new AccountController(getAuthenticatedUserService);

    @Test
    void shouldReturnAuthenticatedUserData() {
        User user = new User();
        user.setId(1L);
        user.setName("Jaime");
        user.setEmail("jaime@test.com");
        user.setRole(UserRole.USER);

        HttpSession session = mock(HttpSession.class);

        when(session.getAttribute("USER_ID"))
                .thenReturn(1L);

        when(getAuthenticatedUserService.getUser(1L))
                .thenReturn(user);

        User result = controller.me(session);

        assertEquals(1L, result.getId());
        assertEquals("Jaime", result.getName());
        assertEquals("jaime@test.com", result.getEmail());
        assertEquals(UserRole.USER, result.getRole());

        verify(getAuthenticatedUserService).getUser(1L);
    }
}