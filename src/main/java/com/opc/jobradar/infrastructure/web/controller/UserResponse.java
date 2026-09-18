package com.opc.jobradar.infrastructure.web.controller;

import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.domain.model.UserRole;

/**
 * Datos públicos de un usuario expuestos por la API.
 */
public record UserResponse(
        Long id,
        String name,
        String email,
        UserRole role
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}