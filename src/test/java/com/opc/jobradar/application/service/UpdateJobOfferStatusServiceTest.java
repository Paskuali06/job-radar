package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferNotFoundException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.domain.model.JobOfferStatusHistory;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import com.opc.jobradar.domain.port.out.JobOfferStatusHistoryRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class UpdateJobOfferStatusServiceTest {

    private static final Long USER_ID = 1L;

    private final JobOfferRepository jobOfferRepository =
            mock(JobOfferRepository.class);

    private final JobOfferStatusHistoryRepository jobOfferStatusHistoryRepository =
            mock(JobOfferStatusHistoryRepository.class);

    private final UpdateJobOfferStatusService updateJobOfferStatusService =
            new UpdateJobOfferStatusService(
                    jobOfferRepository,
                    jobOfferStatusHistoryRepository
            );

    @Test
    void shouldUpdateStatusFromPendingToApplied() {
        Long id = 1L;
        JobOffer jobOffer = jobOfferWithStatus(id, JobOfferStatus.PENDIENTE);

        when(jobOfferRepository.findByIdAndUserId(id, USER_ID))
                .thenReturn(Optional.of(jobOffer));
        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        JobOffer result = updateJobOfferStatusService.updateStatus(
                id,
                USER_ID,
                "SOLICITADA"
        );

        assertSame(jobOffer, result);
        assertEquals(JobOfferStatus.SOLICITADA, result.getStatus());
        verify(jobOfferRepository).findByIdAndUserId(id, USER_ID);
        verify(jobOfferRepository).save(jobOffer);
    }

    @Test
    void shouldUpdateStatusFromPendingToRejected() {
        Long id = 1L;
        JobOffer jobOffer = jobOfferWithStatus(id, JobOfferStatus.PENDIENTE);

        when(jobOfferRepository.findByIdAndUserId(id, USER_ID))
                .thenReturn(Optional.of(jobOffer));
        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        JobOffer result = updateJobOfferStatusService.updateStatus(
                id,
                USER_ID,
                "RECHAZADA"
        );

        assertSame(jobOffer, result);
        assertEquals(JobOfferStatus.RECHAZADA, result.getStatus());
        verify(jobOfferRepository).findByIdAndUserId(id, USER_ID);
        verify(jobOfferRepository).save(jobOffer);
    }

    @Test
    void shouldUpdateStatusFromAppliedToRejected() {
        Long id = 1L;
        JobOffer jobOffer = jobOfferWithStatus(id, JobOfferStatus.SOLICITADA);

        when(jobOfferRepository.findByIdAndUserId(id, USER_ID))
                .thenReturn(Optional.of(jobOffer));
        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        JobOffer result = updateJobOfferStatusService.updateStatus(
                id,
                USER_ID,
                "RECHAZADA"
        );

        assertSame(jobOffer, result);
        assertEquals(JobOfferStatus.RECHAZADA, result.getStatus());
        verify(jobOfferRepository).findByIdAndUserId(id, USER_ID);
        verify(jobOfferRepository).save(jobOffer);
    }

    @Test
    void shouldUpdateStatusFromRejectedToApplied() {
        Long id = 1L;
        JobOffer jobOffer = jobOfferWithStatus(id, JobOfferStatus.RECHAZADA);

        when(jobOfferRepository.findByIdAndUserId(id, USER_ID))
                .thenReturn(Optional.of(jobOffer));
        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        JobOffer result = updateJobOfferStatusService.updateStatus(
                id,
                USER_ID,
                "SOLICITADA"
        );

        assertSame(jobOffer, result);
        assertEquals(JobOfferStatus.SOLICITADA, result.getStatus());
        verify(jobOfferRepository).findByIdAndUserId(id, USER_ID);
        verify(jobOfferRepository).save(jobOffer);
    }

    @Test
    void shouldThrowJobOfferNotFoundExceptionWhenJobOfferDoesNotExist() {
        Long id = 1L;

        when(jobOfferRepository.findByIdAndUserId(id, USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                JobOfferNotFoundException.class,
                () -> updateJobOfferStatusService.updateStatus(
                        id,
                        USER_ID,
                        "SOLICITADA"
                )
        );

        verify(jobOfferRepository).findByIdAndUserId(id, USER_ID);
    }

    @Test
    void shouldThrowJobOfferNotFoundExceptionWhenJobOfferDoesNotBelongToUser() {
        Long id = 1L;

        when(jobOfferRepository.findByIdAndUserId(id, USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                JobOfferNotFoundException.class,
                () -> updateJobOfferStatusService.updateStatus(
                        id,
                        USER_ID,
                        "SOLICITADA"
                )
        );

        verify(jobOfferRepository).findByIdAndUserId(id, USER_ID);
        verify(jobOfferRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verifyNoInteractions(jobOfferStatusHistoryRepository);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> updateJobOfferStatusService.updateStatus(
                        null,
                        USER_ID,
                        "SOLICITADA"
                )
        );

        verifyNoInteractions(jobOfferRepository);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUserIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> updateJobOfferStatusService.updateStatus(
                        1L,
                        null,
                        "SOLICITADA"
                )
        );

        verifyNoInteractions(jobOfferRepository);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenStatusIsInvalid() {
        assertThrows(
                IllegalArgumentException.class,
                () -> updateJobOfferStatusService.updateStatus(
                        1L,
                        USER_ID,
                        "APLICADA"
                )
        );

        verifyNoInteractions(jobOfferRepository);
    }

    @Test
    void shouldRegisterNewStatusInHistoryWhenStatusIsUpdated() {
        Long id = 1L;
        JobOffer jobOffer = jobOfferWithStatus(
                id,
                JobOfferStatus.PENDIENTE
        );

        when(jobOfferRepository.findByIdAndUserId(id, USER_ID))
                .thenReturn(Optional.of(jobOffer));
        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        updateJobOfferStatusService.updateStatus(
                id,
                USER_ID,
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

    @Test
    void shouldRegisterHistoryForEachStatusChange() {
        Long id = 1L;
        JobOffer jobOffer = jobOfferWithStatus(
                id,
                JobOfferStatus.PENDIENTE
        );

        when(jobOfferRepository.findByIdAndUserId(id, USER_ID))
                .thenReturn(Optional.of(jobOffer));
        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        updateJobOfferStatusService.updateStatus(
                id,
                USER_ID,
                "SOLICITADA"
        );

        updateJobOfferStatusService.updateStatus(
                id,
                USER_ID,
                "RECHAZADA"
        );

        verify(jobOfferStatusHistoryRepository, times(2))
                .save(argThat(history ->
                        history.getJobOfferId().equals(id)
                                && history.getStatus() != null
                                && history.getChangedAt() != null
                ));
    }

    @Test
    void shouldNotRegisterHistoryWhenJobOfferDoesNotExist() {
        Long id = 999L;

        when(jobOfferRepository.findByIdAndUserId(id, USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                JobOfferNotFoundException.class,
                () -> updateJobOfferStatusService.updateStatus(
                        id,
                        USER_ID,
                        "SOLICITADA"
                )
        );

        verify(jobOfferRepository).findByIdAndUserId(id, USER_ID);
        verifyNoInteractions(jobOfferStatusHistoryRepository);
    }

    private JobOffer jobOfferWithStatus(
            Long id,
            JobOfferStatus status
    ) {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(id);
        jobOffer.setStatus(status);
        jobOffer.setUserId(USER_ID);
        return jobOffer;
    }
}