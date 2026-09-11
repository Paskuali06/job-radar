package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferNotFoundException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import com.opc.jobradar.domain.port.out.JobOfferStatusHistoryRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class UpdateJobOfferStatusServiceTest {

    private final JobOfferRepository jobOfferRepository =
            mock(JobOfferRepository.class);

    private final JobOfferStatusHistoryRepository jobOfferStatusHistoryRepository =
            mock(JobOfferStatusHistoryRepository.class);

    private final UpdateJobOfferStatusService updateJobOfferStatusService =
            new UpdateJobOfferStatusService(
                    jobOfferRepository,
                    jobOfferStatusHistoryRepository
            );

    // ============================================================
    // CT-008 - PENDIENTE -> SOLICITADA
    // ============================================================

    @Test
    void shouldUpdateStatusFromPendingToApplied() {
        Long id = 1L;
        JobOffer jobOffer = jobOfferWithStatus(id, JobOfferStatus.PENDIENTE);

        when(jobOfferRepository.findById(id))
                .thenReturn(Optional.of(jobOffer));
        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        JobOffer result = updateJobOfferStatusService.updateStatus(
                id,
                "SOLICITADA"
        );

        assertSame(jobOffer, result);
        assertEquals(JobOfferStatus.SOLICITADA, result.getStatus());
        verify(jobOfferRepository).findById(id);
        verify(jobOfferRepository).save(jobOffer);
    }

    // ============================================================
    // CT-008 - PENDIENTE -> RECHAZADA
    // ============================================================

    @Test
    void shouldUpdateStatusFromPendingToRejected() {
        Long id = 1L;
        JobOffer jobOffer = jobOfferWithStatus(id, JobOfferStatus.PENDIENTE);

        when(jobOfferRepository.findById(id))
                .thenReturn(Optional.of(jobOffer));
        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        JobOffer result = updateJobOfferStatusService.updateStatus(
                id,
                "RECHAZADA"
        );

        assertSame(jobOffer, result);
        assertEquals(JobOfferStatus.RECHAZADA, result.getStatus());
        verify(jobOfferRepository).findById(id);
        verify(jobOfferRepository).save(jobOffer);
    }

    // ============================================================
    // CT-008 - SOLICITADA -> RECHAZADA
    // ============================================================

    @Test
    void shouldUpdateStatusFromAppliedToRejected() {
        Long id = 1L;
        JobOffer jobOffer = jobOfferWithStatus(id, JobOfferStatus.SOLICITADA);

        when(jobOfferRepository.findById(id))
                .thenReturn(Optional.of(jobOffer));
        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        JobOffer result = updateJobOfferStatusService.updateStatus(
                id,
                "RECHAZADA"
        );

        assertSame(jobOffer, result);
        assertEquals(JobOfferStatus.RECHAZADA, result.getStatus());
        verify(jobOfferRepository).findById(id);
        verify(jobOfferRepository).save(jobOffer);
    }

    // ============================================================
    // CT-008 - RECHAZADA -> SOLICITADA
    // ============================================================

    @Test
    void shouldUpdateStatusFromRejectedToApplied() {
        Long id = 1L;
        JobOffer jobOffer = jobOfferWithStatus(id, JobOfferStatus.RECHAZADA);

        when(jobOfferRepository.findById(id))
                .thenReturn(Optional.of(jobOffer));
        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        JobOffer result = updateJobOfferStatusService.updateStatus(
                id,
                "SOLICITADA"
        );

        assertSame(jobOffer, result);
        assertEquals(JobOfferStatus.SOLICITADA, result.getStatus());
        verify(jobOfferRepository).findById(id);
        verify(jobOfferRepository).save(jobOffer);
    }

    // ============================================================
    // CT-008 - ID inexistente
    // ============================================================

    @Test
    void shouldThrowJobOfferNotFoundExceptionWhenJobOfferDoesNotExist() {
        Long id = 1L;

        when(jobOfferRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                JobOfferNotFoundException.class,
                () -> updateJobOfferStatusService.updateStatus(
                        id,
                        "SOLICITADA"
                )
        );

        verify(jobOfferRepository).findById(id);
    }

    // ============================================================
    // CT-008 - ID nulo
    // ============================================================

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> updateJobOfferStatusService.updateStatus(
                        null,
                        "SOLICITADA"
                )
        );

        verifyNoInteractions(jobOfferRepository);
    }

    // ============================================================
    // CT-008 - Estado inválido
    // ============================================================

    @Test
    void shouldThrowIllegalArgumentExceptionWhenStatusIsInvalid() {
        assertThrows(
                IllegalArgumentException.class,
                () -> updateJobOfferStatusService.updateStatus(
                        1L,
                        "APLICADA"
                )
        );

        verifyNoInteractions(jobOfferRepository);
    }

    // ============================================================
    // CT-020 - Cambio de estado registra historial
    // ============================================================

    @Test
    void shouldRegisterNewStatusInHistoryWhenStatusIsUpdated() {
        Long id = 1L;
        JobOffer jobOffer = jobOfferWithStatus(
                id,
                JobOfferStatus.PENDIENTE
        );

        when(jobOfferRepository.findById(id))
                .thenReturn(Optional.of(jobOffer));

        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        updateJobOfferStatusService.updateStatus(
                id,
                "SOLICITADA"
        );

        verify(jobOfferStatusHistoryRepository).save(
                argThat(history ->
                        history.getJobOfferId().equals(id)
                                && history.getStatus()
                                == JobOfferStatus.SOLICITADA
                                && history.getChangedAt() != null
                )
        );
    }

    // ============================================================
    // CT-020 - Varios cambios de estado registran varios historiales
    // ============================================================

    @Test
    void shouldRegisterHistoryForEachStatusChange() {
        Long id = 1L;
        JobOffer jobOffer = jobOfferWithStatus(
                id,
                JobOfferStatus.PENDIENTE
        );

        when(jobOfferRepository.findById(id))
                .thenReturn(Optional.of(jobOffer));

        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        updateJobOfferStatusService.updateStatus(
                id,
                "SOLICITADA"
        );

        updateJobOfferStatusService.updateStatus(
                id,
                "RECHAZADA"
        );

        verify(jobOfferStatusHistoryRepository, times(2))
                .save(argThat(history ->
                        history.getJobOfferId().equals(id)
                                && history.getStatus() != null
                                && history.getChangedAt() != null
                ));
    }

    // ============================================================
    // CT-020 - Oferta inexistente no registra historial
    // ============================================================

    @Test
    void shouldNotRegisterHistoryWhenJobOfferDoesNotExist() {
        Long id = 999L;

        when(jobOfferRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                JobOfferNotFoundException.class,
                () -> updateJobOfferStatusService.updateStatus(
                        id,
                        "SOLICITADA"
                )
        );

        verify(jobOfferRepository).findById(id);
        verifyNoInteractions(jobOfferStatusHistoryRepository);
    }

    private JobOffer jobOfferWithStatus(
            Long id,
            JobOfferStatus status
    ) {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(id);
        jobOffer.setStatus(status);
        return jobOffer;
    }
}