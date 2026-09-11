package com.opc.jobradar.infrastructure.web;

import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;

/**
 * Datos recibidos para actualizar una oferta de empleo mediante HTTP.
 */
public record UpdateJobOfferRequest(
        @NotBlank
        String company,

        @NotBlank
        String title,

        String location,

        String workMode,

        @NotBlank
        String url,

        OffsetDateTime publishedAt,

        Integer score,

        String classification,

        String description
) {
}