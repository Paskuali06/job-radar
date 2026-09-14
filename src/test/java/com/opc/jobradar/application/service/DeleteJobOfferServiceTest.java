package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferNotFoundException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeleteJobOfferServiceTest {

    private static final Long JOB_OFFER_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Long OTHER_USER_ID = 2L;

    private final JobOfferRepository jobOfferRepository =
            org.mockito.Mockito.mock(JobOfferRepository.class);

    private final DeleteJobOfferService deleteJobOfferService =
            new DeleteJobOfferService(jobOfferRepository);

    @Test
    void shouldDeleteJobOfferBelongingToUser() {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(JOB_OFFER_ID);
        jobOffer.setUserId(USER_ID);

        when(jobOfferRepository.findByIdAndUserId(
                JOB_OFFER_ID,
                USER_ID
        )).thenReturn(Optional.of(jobOffer));

        deleteJobOfferService.delete(JOB_OFFER_ID, USER_ID);

        verify(jobOfferRepository).deleteById(JOB_OFFER_ID);
    }

    @Test
    void shouldThrowExceptionWhenJobOfferDoesNotBelongToUser() {
        when(jobOfferRepository.findByIdAndUserId(
                JOB_OFFER_ID,
                OTHER_USER_ID
        )).thenReturn(Optional.empty());

        assertThrows(
                JobOfferNotFoundException.class,
                () -> deleteJobOfferService.delete(
                        JOB_OFFER_ID,
                        OTHER_USER_ID
                )
        );

        verify(jobOfferRepository, never()).deleteById(JOB_OFFER_ID);
    }

    @Test
    void shouldThrowExceptionWhenJobOfferDoesNotExist() {
        when(jobOfferRepository.findByIdAndUserId(
                JOB_OFFER_ID,
                USER_ID
        )).thenReturn(Optional.empty());

        assertThrows(
                JobOfferNotFoundException.class,
                () -> deleteJobOfferService.delete(
                        JOB_OFFER_ID,
                        USER_ID
                )
        );

        verify(jobOfferRepository, never()).deleteById(JOB_OFFER_ID);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> deleteJobOfferService.delete(null, USER_ID)
        );

        verify(jobOfferRepository, never())
                .findByIdAndUserId(null, USER_ID);
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> deleteJobOfferService.delete(
                        JOB_OFFER_ID,
                        null
                )
        );

        verify(jobOfferRepository, never())
                .findByIdAndUserId(JOB_OFFER_ID, null);
    }
}