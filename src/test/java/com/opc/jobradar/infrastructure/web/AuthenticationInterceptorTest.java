package com.opc.jobradar.infrastructure.web;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthenticationInterceptorTest {

    private static final String USER_ID = "USER_ID";

    private final AuthenticationInterceptor interceptor =
            new AuthenticationInterceptor();

    @Test
    void shouldRejectRequestWithoutAuthenticatedUser() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean result = interceptor.preHandle(request, response, null);

        assertFalse(result);
        assertTrue(response.getStatus() == 401);
    }

    @Test
    void shouldAllowRequestWithAuthenticatedUser() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        HttpSession session = request.getSession();
        session.setAttribute(USER_ID, 1L);

        boolean result = interceptor.preHandle(request, response, null);

        assertTrue(result);
    }
}