package com.opc.jobradar.infrastructure.persistence.adapter;

import com.opc.jobradar.domain.model.FiltersByJobOffer;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import com.opc.jobradar.infrastructure.persistence.entity.JobOfferEntity;
import com.opc.jobradar.infrastructure.persistence.mapper.JobOfferMapper;
import com.opc.jobradar.infrastructure.persistence.repository.JobOfferJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida que implementa el puerto de ofertas mediante JPA.
 *
 * Convierte el modelo de dominio a entidades JPA antes de persistirlas y
 * realiza la conversión inversa al devolver resultados a la aplicación.
 */
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
    public Optional<JobOffer> findByIdAndUserId(Long id, Long userId) {
        return repository.findByIdAndUserId(id, userId)
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
    public boolean existsByUserIdAndSourceAndExternalId(
            Long userId,
            String source,
            String externalId
    ) {
        return repository.existsByUserIdAndSourceAndExternalId(
                userId,
                source,
                externalId
        );
    }

    @Override
    public List<JobOffer> findAll(
            Long userId,
            FiltersByJobOffer filters
    ) {
        Pageable pageable = createPageable(filters);

        return repository.findAll(
                        userId,
                        filters.getCompany(),
                        filters.getLocation(),
                        filters.getWorkMode(),
                        filters.getStatus() == null
                                ? null
                                : filters.getStatus().name(),
                        filters.getSearch(),
                        filters.getSortBy(),
                        filters.getSortDirection(),
                        pageable
                )
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    private Pageable createPageable(FiltersByJobOffer filters) {
        if (filters.getPage() == null || filters.getSize() == null) {
            return Pageable.unpaged();
        }

        return PageRequest.of(
                filters.getPage(),
                filters.getSize()
        );
    }

    @Override
    public long countByUserId(Long userId) {
        return repository.countByUserId(userId);
    }

    @Override
    public long countByUserIdAndStatus(
            Long userId,
            String status
    ) {
        return repository.countByUserIdAndStatus(userId, status);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}