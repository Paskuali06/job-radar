package com.opc.jobradar.infrastructure.web.controller;

import jakarta.validation.constraints.NotBlank;

/**
 * Datos recibidos para registrar un usuario mediante HTTP.
 */
public record RegisterUserRequest(
        @NotBlank
        String name,

        @NotBlank
        String email,

        @NotBlank
        String password
) {
}