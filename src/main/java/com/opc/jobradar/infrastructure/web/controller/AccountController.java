package com.opc.jobradar.infrastructure.web.controller;

import com.opc.jobradar.application.service.GetAuthenticatedUserService;
import com.opc.jobradar.domain.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AccountController {

    private static final String USER_ID = "USER_ID";

    private final GetAuthenticatedUserService getAuthenticatedUserService;

    public AccountController(
            GetAuthenticatedUserService getAuthenticatedUserService) {
        this.getAuthenticatedUserService = getAuthenticatedUserService;
    }

    @GetMapping("/me")
    public UserResponse me(HttpSession session) {
    Long userId = (Long) session.getAttribute(USER_ID);
    User user = getAuthenticatedUserService.getUser(userId);

    return UserResponse.from(user);
}
}