package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferNotFoundException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateJobOfferServiceTest {

    @Test
    void shouldUpdateJobOffer() {
        JobOfferRepository repository = mock(JobOfferRepository.class);

        JobOffer existingJobOffer = new JobOffer();
        existingJobOffer.setId(1L);
        existingJobOffer.setUserId(1L);
        existingJobOffer.setCompany("Old Company");
        existingJobOffer.setTitle("Old Title");
        existingJobOffer.setLocation("Old Location");
        existingJobOffer.setWorkMode("Presencial");
        existingJobOffer.setUrl("https://old-url.com");

        JobOffer updatedJobOffer = new JobOffer();
        updatedJobOffer.setCompany("New Company");
        updatedJobOffer.setTitle("New Title");
        updatedJobOffer.setLocation("New Location");
        updatedJobOffer.setWorkMode("Remoto");
        updatedJobOffer.setUrl("https://new-url.com");

        when(repository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(existingJobOffer));

        UpdateJobOfferService service = new UpdateJobOfferService(repository);

        service.update(1L, 1L, updatedJobOffer);

        assertEquals("New Company", existingJobOffer.getCompany());
        assertEquals("New Title", existingJobOffer.getTitle());
        assertEquals("New Location", existingJobOffer.getLocation());
        assertEquals("Remoto", existingJobOffer.getWorkMode());
        assertEquals("https://new-url.com", existingJobOffer.getUrl());
        assertEquals(1L, existingJobOffer.getUserId());

        verify(repository).save(existingJobOffer);
    }

    @Test
    void shouldThrowExceptionWhenJobOfferDoesNotBelongToUser() {
        JobOfferRepository repository = mock(JobOfferRepository.class);

        when(repository.findByIdAndUserId(1L, 2L))
                .thenReturn(Optional.empty());

        UpdateJobOfferService service = new UpdateJobOfferService(repository);

        assertThrows(
                JobOfferNotFoundException.class,
                () -> service.update(1L, 2L, new JobOffer())
        );

        verify(repository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        JobOfferRepository repository = mock(JobOfferRepository.class);

        UpdateJobOfferService service = new UpdateJobOfferService(repository);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.update(null, 1L, new JobOffer())
        );

        verify(repository, never()).findByIdAndUserId(null, 1L);
        verify(repository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        JobOfferRepository repository = mock(JobOfferRepository.class);

        UpdateJobOfferService service = new UpdateJobOfferService(repository);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.update(1L, null, new JobOffer())
        );

        verify(repository, never()).findByIdAndUserId(1L, null);
        verify(repository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}