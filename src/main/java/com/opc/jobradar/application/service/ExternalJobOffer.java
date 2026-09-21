package com.opc.jobradar.application.service;

import java.time.OffsetDateTime;

/**
 * Representa una oferta de empleo recibida desde una fuente externa.
 *
 * Este objeto contiene únicamente los datos proporcionados por la fuente.
 * El usuario propietario se determina durante la ingesta.
 */
public record ExternalJobOffer(
        String source,
        String externalId,
        String company,
        String title,
        String location,
        String workMode,
        String url,
        String description,
        OffsetDateTime publishedAt
) {
}