package com.opc.jobradar.infrastructure.web.controller;

import com.opc.jobradar.application.service.LoginUserService;
import com.opc.jobradar.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles user login and logout.
 */
@Tag(
        name = "Authentication",
        description = "Gestión de autenticación de usuarios"
)
@RestController
@RequestMapping("/auth")
public class LoginController {

    private static final String USER_ID = "USER_ID";

    private final LoginUserService loginUserService;

    public LoginController(LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
    }

    /**
     * Authenticates a user and creates an HTTP session.
     *
     * @param email email del usuario
     * @param password contraseña del usuario
     * @param session sesión HTTP
     */
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario y crea una sesión HTTP."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inicio de sesión correcto"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Credenciales no válidas"
            )
    })
    @PostMapping("/login")
    public void login(
            String email,
            String password,
            HttpSession session) {

        User user = loginUserService.login(email, password);

        session.setAttribute(USER_ID, user.getId());
    }

    /**
     * Invalidates the current HTTP session.
     *
     * @param session sesión HTTP actual
     */
    @Operation(
            summary = "Cerrar sesión",
            description = "Invalida la sesión HTTP actual del usuario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sesión cerrada correctamente"
            )
    })
    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }
}