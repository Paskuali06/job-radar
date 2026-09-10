package com.opc.jobradar.infrastructure.web;

/**
 * Datos recibidos para actualizar el estado de una oferta de empleo mediante HTTP.
 */
public record UpdateJobOfferStatusRequest(
        String status
) {
}