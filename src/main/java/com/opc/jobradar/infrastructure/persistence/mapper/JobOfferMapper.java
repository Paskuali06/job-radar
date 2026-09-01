package com.opc.jobradar.infrastructure.persistence.mapper;

import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.infrastructure.persistence.entity.JobOfferEntity;
import org.springframework.stereotype.Component;

@Component
public class JobOfferMapper {

    public JobOffer toDomain(JobOfferEntity entity) {
        if (entity == null) {
            return null;
        }

        return new JobOffer(
                entity.getId(),
                entity.getCompany(),
                entity.getTitle(),
                entity.getLocation(),
                entity.getWorkMode(),
                entity.getUrl(),
                entity.getPublishedAt(),
                entity.getStatus(),
                entity.getScore(),
                entity.getClassification(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public JobOfferEntity toEntity(JobOffer domain) {
        if (domain == null) {
            return null;
        }

        JobOfferEntity entity = new JobOfferEntity();

        entity.setId(domain.getId());
        entity.setCompany(domain.getCompany());
        entity.setTitle(domain.getTitle());
        entity.setLocation(domain.getLocation());
        entity.setWorkMode(domain.getWorkMode());
        entity.setUrl(domain.getUrl());
        entity.setPublishedAt(domain.getPublishedAt());
        entity.setStatus(domain.getStatus());
        entity.setScore(domain.getScore());
        entity.setClassification(domain.getClassification());
        entity.setDescription(domain.getDescription());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }
}