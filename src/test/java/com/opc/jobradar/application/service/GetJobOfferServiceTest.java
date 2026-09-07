package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferNotFoundException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class GetJobOfferServiceTest {

    private final JobOfferRepository jobOfferRepository =
            mock(JobOfferRepository.class);

    private final GetJobOfferService getJobOfferService =
            new GetJobOfferService(jobOfferRepository);

    // ============================================================
    // CT-007 - Devolver una oferta cuando existe para un ID válido
    // ============================================================

    @Test
    void shouldReturnJobOfferWhenIdIsValidAndJobOfferExists() {
        Long id = 1L;
        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(id);

        when(jobOfferRepository.findById(id))
                .thenReturn(Optional.of(jobOffer));

        JobOffer result = getJobOfferService.getById(id);

        assertSame(jobOffer, result);
        verify(jobOfferRepository).findById(id);
    }

    // ============================================================
    // CT-007 - Lanzar excepción cuando no existe la oferta
    // ============================================================

    @Test
    void shouldThrowJobOfferNotFoundExceptionWhenIdIsValidAndJobOfferDoesNotExist() {
        Long id = 1L;

        when(jobOfferRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                JobOfferNotFoundException.class,
                () -> getJobOfferService.getById(id)
        );

        verify(jobOfferRepository).findById(id);
    }

    // ============================================================
    // CT-007 - Rechazar ID nulo sin consultar el repositorio
    // ============================================================

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> getJobOfferService.getById(null)
        );

        verifyNoInteractions(jobOfferRepository);
    }
}