package com.opc.jobradar.application.service;

import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de uso encargado de obtener las métricas del dashboard.
 */
@Service
public class GetDashboardService {

    private final JobOfferRepository repository;

    public GetDashboardService(JobOfferRepository repository) {
        this.repository = repository;
    }

    /**
     * Obtiene las métricas de ofertas del usuario.
     *
     * @param userId identificador del usuario
     * @return métricas del dashboard
     */
    public DashboardResponse getDashboard(Long userId) {
        long totalOffers = repository.countByUserId(userId);
        long pendingOffers = repository.countByUserIdAndStatus(
                userId,
                "PENDIENTE"
        );
        long appliedOffers = repository.countByUserIdAndStatus(
                userId,
                "SOLICITADA"
        );
        long rejectedOffers = repository.countByUserIdAndStatus(
                userId,
                "RECHAZADA"
        );

        return new DashboardResponse(
                totalOffers,
                pendingOffers,
                appliedOffers,
                rejectedOffers
        );
    }
}