package com.opc.jobradar.infrastructure.persistence.adapter;

import com.opc.jobradar.domain.model.JobOfferStatusHistory;
import com.opc.jobradar.domain.port.out.JobOfferStatusHistoryRepository;
import com.opc.jobradar.infrastructure.persistence.entity.JobOfferStatusHistoryEntity;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.infrastructure.persistence.repository.JobOfferStatusHistoryJpaRepository;
import org.springframework.stereotype.Component;

/**
 * Adaptador de salida que persiste el historial de estados mediante JPA.
 */
@Component
public class JobOfferStatusHistoryPersistenceAdapter
        implements JobOfferStatusHistoryRepository {

    private final JobOfferStatusHistoryJpaRepository repository;

    public JobOfferStatusHistoryPersistenceAdapter(
            JobOfferStatusHistoryJpaRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public JobOfferStatusHistory save(JobOfferStatusHistory history) {
        JobOfferStatusHistoryEntity entity =
                new JobOfferStatusHistoryEntity();

        entity.setId(history.getId());
        entity.setJobOfferId(history.getJobOfferId());
        entity.setStatus(history.getStatus().name());
        entity.setChangedAt(history.getChangedAt());

        JobOfferStatusHistoryEntity savedEntity =
                repository.save(entity);

        JobOfferStatusHistory savedHistory =
                new JobOfferStatusHistory();

        savedHistory.setId(savedEntity.getId());
        savedHistory.setJobOfferId(savedEntity.getJobOfferId());
        savedHistory.setStatus(
                JobOfferStatus.valueOf(savedEntity.getStatus())
        );
        savedHistory.setChangedAt(savedEntity.getChangedAt());

        return savedHistory;
    }
}