package com.opc.jobradar.infrastructure.persistence.adapter;

import com.opc.jobradar.domain.model.FiltersByJobOffer;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.infrastructure.persistence.entity.JobOfferEntity;
import com.opc.jobradar.infrastructure.persistence.mapper.JobOfferMapper;
import com.opc.jobradar.infrastructure.persistence.repository.JobOfferJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.opc.jobradar.domain.model.JobOffer;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class JobOfferPersistenceAdapterTest {

    @Mock
    private JobOfferJpaRepository repository;

    @Mock
    private JobOfferMapper mapper;

    @InjectMocks
    private JobOfferPersistenceAdapter adapter;

    @Test
    void shouldTranslateStatusFilterToRepositoryParameter() {
        FiltersByJobOffer filters = new FiltersByJobOffer(
                null,
                null,
                null,
                JobOfferStatus.SOLICITADA
        );

        adapter.findAll(filters);

        verify(repository).findAll(
                null,
                null,
                null,
                "SOLICITADA"
        );
    }
    @Test
    void shouldMapRepositoryResultToDomain() {
    JobOfferEntity entity = new JobOfferEntity();
    entity.setId(1L);
    entity.setCompany("Empresa A");
    entity.setTitle("Backend Developer");
    entity.setLocation("Madrid");
    entity.setWorkMode("REMOTO");
    entity.setStatus("SOLICITADA");
    entity.setUrl("https://example.com/job-a");
    entity.setSource("LinkedIn");
    entity.setExternalId("job-a");

    JobOffer jobOffer = new JobOffer(
            1L,
            "Empresa A",
            "Backend Developer",
            "Madrid",
            "REMOTO",
            "https://example.com/job-a",
            null,
            JobOfferStatus.SOLICITADA,
            null,
            null,
            null,
            null,
            null,
            "LinkedIn",
            "job-a"
    );

    when(repository.findAll(null, null, null, null))
            .thenReturn(List.of(entity));

    when(mapper.toDomain(entity))
            .thenReturn(jobOffer);

        List<JobOffer> result = adapter.findAll(
            new FiltersByJobOffer(null, null, null, null)
    );

    assertEquals(1, result.size());
    assertEquals(jobOffer, result.get(0));
    }
    @Test
    void shouldReturnAllMappedJobOffers() {
    JobOfferEntity firstEntity = new JobOfferEntity();
    JobOfferEntity secondEntity = new JobOfferEntity();

    JobOffer firstJobOffer = new JobOffer(
            1L,
            "Empresa A",
            "Backend Developer",
            "Madrid",
            "REMOTO",
            "https://example.com/job-a",
            null,
            JobOfferStatus.SOLICITADA,
            null,
            null,
            null,
            null,
            null,
            "LinkedIn",
            "job-a"
    );

    JobOffer secondJobOffer = new JobOffer(
            2L,
            "Empresa B",
            "Java Developer",
            "Barcelona",
            "HIBRIDO",
            "https://example.com/job-b",
            null,
            JobOfferStatus.PENDIENTE,
            null,
            null,
            null,
            null,
            null,
            "Indeed",
            "job-b"
    );

    when(repository.findAll(null, null, null, null))
            .thenReturn(List.of(firstEntity, secondEntity));

    when(mapper.toDomain(firstEntity))
            .thenReturn(firstJobOffer);

    when(mapper.toDomain(secondEntity))
            .thenReturn(secondJobOffer);

    List<JobOffer> result = adapter.findAll(
            new FiltersByJobOffer(null, null, null, null)
    );

    assertEquals(2, result.size());
    assertEquals(firstJobOffer, result.get(0));
    assertEquals(secondJobOffer, result.get(1));
}
    @Test
    void shouldReturnEmptyListWhenRepositoryReturnsNoResults() {
    when(repository.findAll(null, null, null, null))
            .thenReturn(List.of());

    List<JobOffer> result = adapter.findAll(
            new FiltersByJobOffer(null, null, null, null)
    );

    assertEquals(0, result.size());
}
}