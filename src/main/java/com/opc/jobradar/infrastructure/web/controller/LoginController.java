package com.opc.jobradar.infrastructure.web.controller;

import com.opc.jobradar.application.service.LoginUserService;
import com.opc.jobradar.domain.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles user login.
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    private static final String USER_ID = "USER_ID";

    private final LoginUserService loginUserService;

    public LoginController(LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
    }

    @PostMapping("/login")
    public void login(
            String email,
            String password,
            HttpSession session) {

        User user = loginUserService.login(email, password);

        session.setAttribute(USER_ID, user.getId());
    }
    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }
}