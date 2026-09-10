package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferNotFoundException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeleteJobOfferServiceTest {

    @Test
    void shouldDeleteJobOffer() {
        JobOfferRepository repository = mock(JobOfferRepository.class);

        when(repository.findById(1L))
                .thenReturn(Optional.of(mock(JobOffer.class)));

        DeleteJobOfferService service = new DeleteJobOfferService(repository);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenJobOfferDoesNotExist() {
        JobOfferRepository repository = mock(JobOfferRepository.class);

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        DeleteJobOfferService service = new DeleteJobOfferService(repository);

        assertThrows(
                JobOfferNotFoundException.class,
                () -> service.delete(1L)
        );

        verify(repository, never()).deleteById(1L);
    }
}