package com.opc.jobradar.infrastructure.persistence.repository;

import com.opc.jobradar.infrastructure.persistence.entity.JobOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobOfferJpaRepository extends JpaRepository<JobOfferEntity, Long> {

    boolean existsByUrl(String url);

    Optional<JobOfferEntity> findByUrl(String url);
}