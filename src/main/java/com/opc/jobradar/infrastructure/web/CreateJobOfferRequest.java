package com.opc.jobradar.infrastructure.web;

/**
 * Datos recibidos para crear una oferta de empleo mediante HTTP.
 */
public record CreateJobOfferRequest(
        String company,
        String title,
        String location,
        String workMode,
        String url,
        String source,
        String externalId
) {
}