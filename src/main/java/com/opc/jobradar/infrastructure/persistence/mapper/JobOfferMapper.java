package com.opc.jobradar.infrastructure.persistence.mapper;

import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.infrastructure.persistence.entity.JobOfferEntity;
import org.springframework.stereotype.Component;

/**
 * Convierte ofertas entre el modelo de dominio y la entidad de persistencia.
 *
 * Centraliza esta conversión para evitar que los detalles de JPA se filtren
 * hacia las capas internas.
 */
@Component
public class JobOfferMapper {

    /**
     * Convierte una entidad JPA en una oferta de dominio.
     *
     * @param entity entidad obtenida de persistencia
     * @return oferta de dominio equivalente, o {@code null} si la entidad es
     *         {@code null}
     */
    public JobOffer toDomain(JobOfferEntity entity) {
        if (entity == null) {
            return null;
        }

        JobOffer jobOffer = new JobOffer(
                entity.getId(),
                entity.getCompany(),
                entity.getTitle(),
                entity.getLocation(),
                entity.getWorkMode(),
                entity.getUrl(),
                entity.getPublishedAt(),
                entity.getStatus() == null
                        ? null
                        : JobOfferStatus.valueOf(entity.getStatus()),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getSource(),
                entity.getExternalId()
        );

        jobOffer.setUserId(entity.getUserId());

        return jobOffer;
    }

    /**
     * Convierte una oferta de dominio en una entidad JPA.
     *
     * @param domain oferta de dominio que se quiere persistir
     * @return entidad equivalente, o {@code null} si la oferta es
     *         {@code null}
     */
    public JobOfferEntity toEntity(JobOffer domain) {
        if (domain == null) {
            return null;
        }

        JobOfferEntity entity = new JobOfferEntity();

        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setCompany(domain.getCompany());
        entity.setTitle(domain.getTitle());
        entity.setLocation(domain.getLocation());
        entity.setWorkMode(domain.getWorkMode());
        entity.setUrl(domain.getUrl());
        entity.setPublishedAt(domain.getPublishedAt());
        entity.setStatus(
                domain.getStatus() == null
                        ? null
                        : domain.getStatus().name()
        );
        entity.setDescription(domain.getDescription());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setSource(domain.getSource());
        entity.setExternalId(domain.getExternalId());

        return entity;
    }
}