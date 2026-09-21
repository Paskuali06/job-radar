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
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobOfferPersistenceAdapterTest {

    private static final Long USER_ID = 1L;

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

        adapter.findAll(USER_ID, filters);

        verify(repository).findAll(
                USER_ID,
                null,
                null,
                null,
                "SOLICITADA",
                null,
                null,
                null,
                Pageable.unpaged()
        );
    }

    @Test
    void shouldTranslateSearchFilterToRepositoryParameter() {
        FiltersByJobOffer filters = new FiltersByJobOffer(
                null,
                null,
                null,
                null
        );

        filters.setSearch("java");

        adapter.findAll(USER_ID, filters);

        verify(repository).findAll(
                USER_ID,
                null,
                null,
                null,
                null,
                "java",
                null,
                null,
                Pageable.unpaged()
        );
    }

    @Test
    void shouldTranslateSortParametersToRepositoryParameters() {
        FiltersByJobOffer filters = new FiltersByJobOffer(
                null,
                null,
                null,
                null
        );

        filters.setSortBy("createdAt");
        filters.setSortDirection("desc");

        adapter.findAll(USER_ID, filters);

        verify(repository).findAll(
                USER_ID,
                null,
                null,
                null,
                null,
                null,
                "createdAt",
                "desc",
                Pageable.unpaged()
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
                "LinkedIn",
                "job-a"
        );

        when(repository.findAll(
                USER_ID,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Pageable.unpaged()
        )).thenReturn(List.of(entity));

        when(mapper.toDomain(entity))
                .thenReturn(jobOffer);

        List<JobOffer> result = adapter.findAll(
                USER_ID,
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
                "Indeed",
                "job-b"
        );

        when(repository.findAll(
                USER_ID,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Pageable.unpaged()
        )).thenReturn(List.of(firstEntity, secondEntity));

        when(mapper.toDomain(firstEntity))
                .thenReturn(firstJobOffer);

        when(mapper.toDomain(secondEntity))
                .thenReturn(secondJobOffer);

        List<JobOffer> result = adapter.findAll(
                USER_ID,
                new FiltersByJobOffer(null, null, null, null)
        );

        assertEquals(2, result.size());
        assertEquals(firstJobOffer, result.get(0));
        assertEquals(secondJobOffer, result.get(1));
    }

    @Test
    void shouldReturnEmptyListWhenRepositoryReturnsNoResults() {
        when(repository.findAll(
                USER_ID,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Pageable.unpaged()
        )).thenReturn(List.of());

        List<JobOffer> result = adapter.findAll(
                USER_ID,
                new FiltersByJobOffer(null, null, null, null)
        );

        assertEquals(0, result.size());
    }
}