package com.opc.jobradar.application.service;

/**
 * Respuesta con las métricas principales del dashboard.
 *
 * @param totalOffers número total de ofertas del usuario
 * @param pendingOffers número de ofertas pendientes
 * @param appliedOffers número de ofertas solicitadas
 * @param rejectedOffers número de ofertas rechazadas
 */
public record DashboardResponse(
        long totalOffers,
        long pendingOffers,
        long appliedOffers,
        long rejectedOffers
) {
}