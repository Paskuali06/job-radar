package com.opc.jobradar.infrastructure.persistence.repository;

import com.opc.jobradar.infrastructure.persistence.entity.JobOfferStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para el historial de estados de ofertas.
 */
public interface JobOfferStatusHistoryJpaRepository
        extends JpaRepository<JobOfferStatusHistoryEntity, Long> {
}