package com.opc.jobradar.application.service;

import com.opc.jobradar.domain.model.FiltersByJobOffer;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetJobOffersServiceTest {

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

        when(jobOfferRepository.findAll(filters))
                .thenReturn(jobOffers);

        List<JobOffer> result = getJobOffersService.get(filters);

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

        when(jobOfferRepository.findAll(filters))
                .thenReturn(List.of(googleOffer));

        List<JobOffer> result = getJobOffersService.get(filters);

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

        when(jobOfferRepository.findAll(filters))
                .thenReturn(List.of(madridOffer));

        List<JobOffer> result = getJobOffersService.get(filters);

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

        when(jobOfferRepository.findAll(filters))
                .thenReturn(List.of(remoteOffer));

        List<JobOffer> result = getJobOffersService.get(filters);

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

        when(jobOfferRepository.findAll(filters))
                .thenReturn(List.of(requestedOffer));

        List<JobOffer> result = getJobOffersService.get(filters);

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

        when(jobOfferRepository.findAll(filters))
                .thenReturn(List.of(remoteMadridOffer));

        List<JobOffer> result = getJobOffersService.get(filters);

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

        when(jobOfferRepository.findAll(filters))
                .thenReturn(List.of(requestedRemoteMadridOffer));

        List<JobOffer> result = getJobOffersService.get(filters);

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

        when(jobOfferRepository.findAll(filters))
                .thenReturn(List.of(matchingOffer));

        List<JobOffer> result = getJobOffersService.get(filters);

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

        when(jobOfferRepository.findAll(madridFilters))
                .thenReturn(List.of(
                        remoteMadridRequested,
                        hybridMadridRequested
                ));

        when(jobOfferRepository.findAll(madridRemoteFilters))
                .thenReturn(List.of(
                        remoteMadridRequested
                ));

        when(jobOfferRepository.findAll(madridRemoteRequestedFilters))
                .thenReturn(List.of(
                        remoteMadridRequested
                ));

        List<JobOffer> madridResults =
                getJobOffersService.get(madridFilters);

        List<JobOffer> madridRemoteResults =
                getJobOffersService.get(madridRemoteFilters);

        List<JobOffer> madridRemoteRequestedResults =
                getJobOffersService.get(madridRemoteRequestedFilters);

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

        when(jobOfferRepository.findAll(filters))
                .thenReturn(List.of(madridOffer));

        List<JobOffer> result = getJobOffersService.get(filters);

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

        when(jobOfferRepository.findAll(filters))
                .thenReturn(List.of());

        List<JobOffer> result = getJobOffersService.get(filters);

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

        when(jobOfferRepository.findAll(filters))
                .thenReturn(List.of(
                        madridOffer,
                        barcelonaOffer
                ));

        List<JobOffer> result = getJobOffersService.get(filters);

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

        when(jobOfferRepository.findAll(filters))
                .thenReturn(List.of(requestedMadridOffer));

        List<JobOffer> result = getJobOffersService.get(filters);

        assertEquals(1, result.size());
        assertSame(requestedMadridOffer, result.get(0));
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