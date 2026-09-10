package com.opc.jobradar.infrastructure.web;

import java.time.OffsetDateTime;

/**
 * Datos recibidos para actualizar una oferta de empleo mediante HTTP.
 */
public record UpdateJobOfferRequest(
        String company,
        String title,
        String location,
        String workMode,
        String url,
        OffsetDateTime publishedAt,
        Integer score,
        String classification,
        String description
) {
}