package com.opc.jobradar.infrastructure.web.controller;

import com.opc.jobradar.application.service.GetAuthenticatedUserService;
import com.opc.jobradar.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles authenticated user information.
 */
@Tag(
        name = "Authentication",
        description = "Gestión de autenticación de usuarios"
)
@RestController
@RequestMapping("/auth")
public class AccountController {

    private static final String USER_ID = "USER_ID";

    private final GetAuthenticatedUserService getAuthenticatedUserService;

    public AccountController(
            GetAuthenticatedUserService getAuthenticatedUserService) {
        this.getAuthenticatedUserService = getAuthenticatedUserService;
    }

    /**
     * Gets the currently authenticated user.
     *
     * @param session HTTP session of the authenticated user
     * @return authenticated user information
     */
    @Operation(
            summary = "Obtener usuario autenticado",
            description = "Obtiene la información del usuario actualmente autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario obtenido correctamente"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuario no autenticado"
            )
    })
    @GetMapping("/me")
    public UserResponse me(HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID);
        User user = getAuthenticatedUserService.getUser(userId);

        return UserResponse.from(user);
    }
}