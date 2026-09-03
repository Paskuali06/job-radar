package com.opc.jobradar.infrastructure.persistence.adapter;

import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import com.opc.jobradar.infrastructure.persistence.entity.JobOfferEntity;
import com.opc.jobradar.infrastructure.persistence.mapper.JobOfferMapper;
import com.opc.jobradar.infrastructure.persistence.repository.JobOfferJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JobOfferPersistenceAdapter implements JobOfferRepository {

    private final JobOfferJpaRepository repository;
    private final JobOfferMapper mapper;

    public JobOfferPersistenceAdapter(
            JobOfferJpaRepository repository,
            JobOfferMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public JobOffer save(JobOffer jobOffer) {
        JobOfferEntity entity = mapper.toEntity(jobOffer);
        JobOfferEntity savedEntity = repository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<JobOffer> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<JobOffer> findByUrl(String url) {
        return repository.findByUrl(url)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByUrl(String url) {
        return repository.existsByUrl(url);
    }

    @Override
    public boolean existsBySourceAndExternalId(
            String source,
            String externalId
    ) {
        return repository.existsBySourceAndExternalId(source, externalId);
    }
}