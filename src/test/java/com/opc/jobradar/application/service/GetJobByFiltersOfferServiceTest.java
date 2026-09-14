package com.opc.jobradar.application.service;

import com.opc.jobradar.domain.model.FiltersByJobOffer;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetJobOffersServiceTest {

    private static final Long USER_ID = 1L;

    private final JobOfferRepository jobOfferRepository =
            mock(JobOfferRepository.class);

    private final GetJobOffersService getJobOffersService =
            new GetJobOffersService(jobOfferRepository);

    // ============================================================
    // CT-009 - Sin filtros devuelve todas las ofertas
    // ============================================================

    @Test
    void shouldReturnAllJobOffersWhenNoFiltersAreProvided() {
        JobOffer madridOffer = createJobOffer(
                1L,
                "Google",
                "Madrid",
                "REMOTO",
                JobOfferStatus.SOLICITADA
        );

        JobOffer barcelonaOffer = createJobOffer(
                2L,
                "Amazon",
                "Barcelona",
                "HIBRIDO",
                JobOfferStatus.PENDIENTE
        );

        List<JobOffer> jobOffers = List.of(
                madridOffer,
                barcelonaOffer
        );

        FiltersByJobOffer filters = new FiltersByJobOffer(
                null,
                null,
                null,
                null
        );

        when(jobOfferRepository.findAll(
                eq(USER_ID),
                any(FiltersByJobOffer.class)
        )).thenReturn(jobOffers);

        List<JobOffer> result =
                getJobOffersService.get(USER_ID, filters);

        assertEquals(2, result.size());
        assertSame(madridOffer, result.get(0));
        assertSame(barcelonaOffer, result.get(1));
    }

    // ============================================================
    // CT-009 - Filtrar por empresa
    // ============================================================

    @Test
    void shouldFilterJobOffersByCompany() {
        JobOffer googleOffer = createJobOffer(
                1L,
                "Google",
                "Madrid",
                "REMOTO",
                JobOfferStatus.SOLICITADA
        );

        FiltersByJobOffer filters = new FiltersByJobOffer(
                "Google",
                null,
                null,
                null
        );

        when(jobOfferRepository.findAll(
                eq(USER_ID),
                any(FiltersByJobOffer.class)
        )).thenReturn(List.of(googleOffer));

        List<JobOffer> result =
                getJobOffersService.get(USER_ID, filters);

        assertEquals(1, result.size());
        assertSame(googleOffer, result.get(0));
    }

    // ============================================================
    // CT-009 - Filtrar por ubicación
    // ============================================================

    @Test
    void shouldFilterJobOffersByLocation() {
        JobOffer madridOffer = createJobOffer(
                1L,
                "Google",
                "Madrid",
                "REMOTO",
                JobOfferStatus.SOLICITADA
        );

        FiltersByJobOffer filters = new FiltersByJobOffer(
                null,
                "Madrid",
                null,
                null
        );

        when(jobOfferRepository.findAll(
                eq(USER_ID),
                any(FiltersByJobOffer.class)
        )).thenReturn(List.of(madridOffer));

        List<JobOffer> result =
                getJobOffersService.get(USER_ID, filters);

        assertEquals(1, result.size());
        assertSame(madridOffer, result.get(0));
    }

    // ============================================================
    // CT-009 - Filtrar por modalidad
    // ============================================================

    @Test
    void shouldFilterJobOffersByWorkMode() {
        JobOffer remoteOffer = createJobOffer(
                1L,
                "Google",
                "Madrid",
                "REMOTO",
                JobOfferStatus.SOLICITADA
        );

        FiltersByJobOffer filters = new FiltersByJobOffer(
                null,
                null,
                "REMOTO",
                null
        );

        when(jobOfferRepository.findAll(
                eq(USER_ID),
                any(FiltersByJobOffer.class)
        )).thenReturn(List.of(remoteOffer));

        List<JobOffer> result =
                getJobOffersService.get(USER_ID, filters);

        assertEquals(1, result.size());
        assertSame(remoteOffer, result.get(0));
    }

    // ============================================================
    // CT-009 - Filtrar por estado
    // ============================================================

    @Test
    void shouldFilterJobOffersByStatus() {
        JobOffer requestedOffer = createJobOffer(
                1L,
                "Google",
                "Madrid",
                "REMOTO",
                JobOfferStatus.SOLICITADA
        );

        FiltersByJobOffer filters = new FiltersByJobOffer(
                null,
                null,
                null,
                JobOfferStatus.SOLICITADA
        );

        when(jobOfferRepository.findAll(
                eq(USER_ID),
                any(FiltersByJobOffer.class)
        )).thenReturn(List.of(requestedOffer));

        List<JobOffer> result =
                getJobOffersService.get(USER_ID, filters);

        assertEquals(1, result.size());
        assertSame(requestedOffer, result.get(0));
    }

    // ============================================================
    // CT-009 - Combinar dos filtros
    // ============================================================

    @Test
    void shouldFilterJobOffersByLocationAndWorkMode() {
        JobOffer remoteMadridOffer = createJobOffer(
                1L,
                "Google",
                "Madrid",
                "REMOTO",
                JobOfferStatus.SOLICITADA
        );

        FiltersByJobOffer filters = new FiltersByJobOffer(
                null,
                "Madrid",
                "REMOTO",
                null
        );

        when(jobOfferRepository.findAll(
                eq(USER_ID),
                any(FiltersByJobOffer.class)
        )).thenReturn(List.of(remoteMadridOffer));

        List<JobOffer> result =
                getJobOffersService.get(USER_ID, filters);

        assertEquals(1, result.size());
        assertSame(remoteMadridOffer, result.get(0));
    }

    // ============================================================
    // CT-009 - Combinar tres filtros
    // ============================================================

    @Test
    void shouldFilterJobOffersByLocationWorkModeAndStatus() {
        JobOffer requestedRemoteMadridOffer = createJobOffer(
                1L,
                "Google",
                "Madrid",
                "REMOTO",
                JobOfferStatus.SOLICITADA
        );

        FiltersByJobOffer filters = new FiltersByJobOffer(
                null,
                "Madrid",
                "REMOTO",
                JobOfferStatus.SOLICITADA
        );

        when(jobOfferRepository.findAll(
                eq(USER_ID),
                any(FiltersByJobOffer.class)
        )).thenReturn(List.of(requestedRemoteMadridOffer));

        List<JobOffer> result =
                getJobOffersService.get(USER_ID, filters);

        assertEquals(1, result.size());
        assertSame(requestedRemoteMadridOffer, result.get(0));
    }

    // ============================================================
    // CT-009 - Combinar los cuatro filtros
    // ============================================================

    @Test
    void shouldFilterJobOffersByAllFilters() {
        JobOffer matchingOffer = createJobOffer(
                1L,
                "Google",
                "Madrid",
                "REMOTO",
                JobOfferStatus.SOLICITADA
        );

        FiltersByJobOffer filters = new FiltersByJobOffer(
                "Google",
                "Madrid",
                "REMOTO",
                JobOfferStatus.SOLICITADA
        );

        when(jobOfferRepository.findAll(
                eq(USER_ID),
                any(FiltersByJobOffer.class)
        )).thenReturn(List.of(matchingOffer));

        List<JobOffer> result =
                getJobOffersService.get(USER_ID, filters);

        assertEquals(1, result.size());
        assertSame(matchingOffer, result.get(0));
    }

    // ============================================================
    // CT-009 - Los filtros se combinan mediante AND
    // ============================================================

    @Test
    void shouldNarrowResultsWhenAddingFilters() {
        JobOffer remoteMadridRequested = createJobOffer(
                1L,
                "Google",
                "Madrid",
                "REMOTO",
                JobOfferStatus.SOLICITADA
        );

        JobOffer hybridMadridRequested = createJobOffer(
                2L,
                "Amazon",
                "Madrid",
                "HIBRIDO",
                JobOfferStatus.SOLICITADA
        );

        FiltersByJobOffer madridFilters = new FiltersByJobOffer(
                null,
                "Madrid",
                null,
                null
        );

        FiltersByJobOffer madridRemoteFilters = new FiltersByJobOffer(
                null,
                "Madrid",
                "REMOTO",
                null
        );

        FiltersByJobOffer madridRemoteRequestedFilters =
                new FiltersByJobOffer(
                        null,
                        "Madrid",
                        "REMOTO",
                        JobOfferStatus.SOLICITADA
                );

        when(jobOfferRepository.findAll(
                eq(USER_ID),
                argThat(filters ->
                        filters != null
                                && "Madrid".equals(filters.getLocation())
                                && filters.getWorkMode() == null
                                && filters.getStatus() == null
                )
        )).thenReturn(List.of(
                remoteMadridRequested,
                hybridMadridRequested
        ));

        when(jobOfferRepository.findAll(
                eq(USER_ID),
                argThat(filters ->
                        filters != null
                                && "Madrid".equals(filters.getLocation())
                                && "REMOTO".equals(filters.getWorkMode())
                                && filters.getStatus() == null
                )
        )).thenReturn(List.of(
                remoteMadridRequested
        ));

        when(jobOfferRepository.findAll(
                eq(USER_ID),
                argThat(filters ->
                        filters != null
                                && "Madrid".equals(filters.getLocation())
                                && "REMOTO".equals(filters.getWorkMode())
                                && filters.getStatus() == JobOfferStatus.SOLICITADA
                )
        )).thenReturn(List.of(
                remoteMadridRequested
        ));

        List<JobOffer> madridResults =
                getJobOffersService.get(USER_ID, madridFilters);

        List<JobOffer> madridRemoteResults =
                getJobOffersService.get(USER_ID, madridRemoteFilters);

        List<JobOffer> madridRemoteRequestedResults =
                getJobOffersService.get(
                        USER_ID,
                        madridRemoteRequestedFilters
                );

        assertEquals(2, madridResults.size());
        assertEquals(1, madridRemoteResults.size());
        assertEquals(1, madridRemoteRequestedResults.size());
        assertSame(
                remoteMadridRequested,
                madridRemoteRequestedResults.get(0)
        );
    }

    // ============================================================
    // CT-009 - Coincidencia exacta
    // ============================================================

    @Test
    void shouldUseExactMatchForLocation() {
        JobOffer madridOffer = createJobOffer(
                1L,
                "Google",
                "Madrid",
                "REMOTO",
                JobOfferStatus.SOLICITADA
        );

        FiltersByJobOffer filters = new FiltersByJobOffer(
                null,
                "Madrid",
                null,
                null
        );

        when(jobOfferRepository.findAll(
                eq(USER_ID),
                any(FiltersByJobOffer.class)
        )).thenReturn(List.of(madridOffer));

        List<JobOffer> result =
                getJobOffersService.get(USER_ID, filters);

        assertEquals(1, result.size());
        assertEquals("Madrid", result.get(0).getLocation());
    }

    // ============================================================
    // CT-009 - Ninguna coincidencia
    // ============================================================

    @Test
    void shouldReturnEmptyListWhenNoJobOfferMatches() {
        FiltersByJobOffer filters = new FiltersByJobOffer(
                null,
                "Valencia",
                null,
                null
        );

        when(jobOfferRepository.findAll(
                eq(USER_ID),
                any(FiltersByJobOffer.class)
        )).thenReturn(List.of());

        List<JobOffer> result =
                getJobOffersService.get(USER_ID, filters);

        assertEquals(0, result.size());
    }

    // ============================================================
    // CT-009 - Filtro vacío equivale a ausencia de filtro
    // ============================================================

    @Test
    void shouldTreatEmptyFilterAsNoFilter() {
        JobOffer madridOffer = createJobOffer(
                1L,
                "Google",
                "Madrid",
                "REMOTO",
                JobOfferStatus.SOLICITADA
        );

        JobOffer barcelonaOffer = createJobOffer(
                2L,
                "Amazon",
                "Barcelona",
                "HIBRIDO",
                JobOfferStatus.PENDIENTE
        );

        FiltersByJobOffer filters = new FiltersByJobOffer(
                "",
                null,
                null,
                null
        );

        when(jobOfferRepository.findAll(
                eq(USER_ID),
                any(FiltersByJobOffer.class)
        )).thenReturn(List.of(
                madridOffer,
                barcelonaOffer
        ));

        List<JobOffer> result =
                getJobOffersService.get(USER_ID, filters);

        assertEquals(2, result.size());
    }

    // ============================================================
    // CT-009 - Filtro vacío + filtro válido
    // ============================================================

    @Test
    void shouldIgnoreEmptyFilterWhenAnotherFilterIsProvided() {
        JobOffer requestedMadridOffer = createJobOffer(
                1L,
                "Google",
                "Madrid",
                "REMOTO",
                JobOfferStatus.SOLICITADA
        );

        FiltersByJobOffer filters = new FiltersByJobOffer(
                "",
                null,
                null,
                JobOfferStatus.SOLICITADA
        );

        when(jobOfferRepository.findAll(
                eq(USER_ID),
                any(FiltersByJobOffer.class)
        )).thenReturn(List.of(requestedMadridOffer));

        List<JobOffer> result =
                getJobOffersService.get(USER_ID, filters);

        assertEquals(1, result.size());
        assertSame(requestedMadridOffer, result.get(0));
    }

    // ============================================================
    // CT-009 - Normalización de filtros vacíos
    // ============================================================

    @Test
    void shouldNormalizeEmptyFiltersBeforeCallingRepository() {
        FiltersByJobOffer filters = new FiltersByJobOffer(
                "",
                "Madrid",
                "",
                null
        );

        getJobOffersService.get(USER_ID, filters);

        ArgumentCaptor<FiltersByJobOffer> captor =
                ArgumentCaptor.forClass(FiltersByJobOffer.class);

        verify(jobOfferRepository).findAll(
                eq(USER_ID),
                captor.capture()
        );

        FiltersByJobOffer capturedFilters = captor.getValue();

        assertEquals(null, capturedFilters.getCompany());
        assertEquals("Madrid", capturedFilters.getLocation());
        assertEquals(null, capturedFilters.getWorkMode());
        assertEquals(null, capturedFilters.getStatus());
    }

    // ============================================================
    // CT-028 - El userId se utiliza para buscar las ofertas
    // ============================================================

    @Test
    void shouldUseAuthenticatedUserIdWhenSearchingJobOffers() {
        FiltersByJobOffer filters = new FiltersByJobOffer(
                null,
                null,
                null,
                null
        );

        getJobOffersService.get(10L, filters);

        verify(jobOfferRepository).findAll(
                eq(10L),
                any(FiltersByJobOffer.class)
        );
    }

    private JobOffer createJobOffer(
            Long id,
            String company,
            String location,
            String workMode,
            JobOfferStatus status
    ) {
        JobOffer jobOffer = new JobOffer();

        jobOffer.setId(id);
        jobOffer.setCompany(company);
        jobOffer.setLocation(location);
        jobOffer.setWorkMode(workMode);
        jobOffer.setStatus(status);

        return jobOffer;
    }
}