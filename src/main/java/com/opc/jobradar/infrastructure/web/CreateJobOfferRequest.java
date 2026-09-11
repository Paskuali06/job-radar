package com.opc.jobradar.infrastructure.web;

import jakarta.validation.constraints.NotBlank;

/**
 * Datos recibidos para crear una oferta de empleo mediante HTTP.
 */
public record CreateJobOfferRequest(
        @NotBlank
        String company,

        @NotBlank
        String title,

        String location,

        String workMode,

        @NotBlank
        String url,

        @NotBlank
        String source,

        @NotBlank
        String externalId
) {
}