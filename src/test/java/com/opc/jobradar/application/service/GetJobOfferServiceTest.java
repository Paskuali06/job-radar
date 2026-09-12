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
        Long userId = 10L;

        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(id);
        jobOffer.setUserId(userId);

        when(jobOfferRepository.findByIdAndUserId(id, userId))
                .thenReturn(Optional.of(jobOffer));

        JobOffer result = getJobOfferService.getById(id, userId);

        assertSame(jobOffer, result);
        verify(jobOfferRepository).findByIdAndUserId(id, userId);
    }

    // ============================================================
    // CT-007 - Lanzar excepción cuando no existe la oferta
    // ============================================================

    @Test
    void shouldThrowJobOfferNotFoundExceptionWhenIdIsValidAndJobOfferDoesNotExist() {
        Long id = 1L;
        Long userId = 10L;

        when(jobOfferRepository.findByIdAndUserId(id, userId))
                .thenReturn(Optional.empty());

        assertThrows(
                JobOfferNotFoundException.class,
                () -> getJobOfferService.getById(id, userId)
        );

        verify(jobOfferRepository).findByIdAndUserId(id, userId);
    }

    // ============================================================
    // CT-007 - Rechazar ID nulo sin consultar el repositorio
    // ============================================================

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIdIsNull() {
        Long userId = 10L;

        assertThrows(
                IllegalArgumentException.class,
                () -> getJobOfferService.getById(null, userId)
        );

        verifyNoInteractions(jobOfferRepository);
    }

    // ============================================================
    // CT-028 - Rechazar usuario nulo sin consultar el repositorio
    // ============================================================

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUserIdIsNull() {
        Long id = 1L;

        assertThrows(
                IllegalArgumentException.class,
                () -> getJobOfferService.getById(id, null)
        );

        verifyNoInteractions(jobOfferRepository);
    }

    // ============================================================
    // CT-028 - Devolver una oferta cuando pertenece al usuario
    // ============================================================

    @Test
    void shouldReturnJobOfferWhenItBelongsToAuthenticatedUser() {
        Long id = 1L;
        Long userId = 10L;

        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(id);
        jobOffer.setUserId(userId);

        when(jobOfferRepository.findByIdAndUserId(id, userId))
                .thenReturn(Optional.of(jobOffer));

        JobOffer result = getJobOfferService.getById(id, userId);

        assertSame(jobOffer, result);
        verify(jobOfferRepository).findByIdAndUserId(id, userId);
    }

    // ============================================================
    // CT-028 - No devolver una oferta de otro usuario
    // ============================================================

    @Test
    void shouldThrowJobOfferNotFoundExceptionWhenJobOfferBelongsToAnotherUser() {
        Long id = 1L;
        Long userId = 10L;

        when(jobOfferRepository.findByIdAndUserId(id, userId))
                .thenReturn(Optional.empty());

        assertThrows(
                JobOfferNotFoundException.class,
                () -> getJobOfferService.getById(id, userId)
        );

        verify(jobOfferRepository).findByIdAndUserId(id, userId);
    }
}