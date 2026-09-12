package com.opc.jobradar.infrastructure.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Protects endpoints that require an authenticated user.
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String USER_ID = "USER_ID";

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        if (request.getSession(false) == null ||
                request.getSession(false).getAttribute(USER_ID) == null) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }
}