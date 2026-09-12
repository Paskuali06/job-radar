package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferAlreadyExistsException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import com.opc.jobradar.domain.port.out.JobOfferStatusHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class CreateJobOfferServiceTest {

    private final JobOfferRepository jobOfferRepository =
            mock(JobOfferRepository.class);

    private final JobOfferStatusHistoryRepository jobOfferStatusHistoryRepository =
            mock(JobOfferStatusHistoryRepository.class);

    private final CreateJobOfferService createJobOfferService =
            new CreateJobOfferService(
                    jobOfferRepository,
                    jobOfferStatusHistoryRepository
            );

    // ============================================================
    // CT-001 - Crear oferta cuando la identidad no existe
    // ============================================================

    @Test
    void shouldCreateJobOfferWhenSourceAndExternalIdDoNotExist() {
        JobOffer jobOffer = createJobOffer();

        when(jobOfferRepository.existsByUserIdAndSourceAndExternalId(
                1L,
                "LinkedIn",
                "123"
        )).thenReturn(false);

        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        JobOffer result = createJobOfferService.create(jobOffer);

        assertSame(jobOffer, result);
        assertEquals(JobOfferStatus.PENDIENTE, result.getStatus());

        verify(jobOfferRepository).existsByUserIdAndSourceAndExternalId(
                1L,
                "LinkedIn",
                "123"
        );

        verify(jobOfferRepository).save(jobOffer);
    }

    // ============================================================
    // CT-016 - No crear oferta duplicada
    // ============================================================

    @Test
    void shouldThrowJobOfferAlreadyExistsExceptionWhenIdentityAlreadyExists() {
        JobOffer jobOffer = createJobOffer();

        when(jobOfferRepository.existsByUserIdAndSourceAndExternalId(
                1L,
                "LinkedIn",
                "123"
        )).thenReturn(true);

        assertThrows(
                JobOfferAlreadyExistsException.class,
                () -> createJobOfferService.create(jobOffer)
        );

        verify(jobOfferRepository).existsByUserIdAndSourceAndExternalId(
                1L,
                "LinkedIn",
                "123"
        );

        verify(jobOfferRepository, never()).save(any(JobOffer.class));
    }

    // ============================================================
    // CT-018 - Validaciones de entrada
    // ============================================================

    @Test
    void shouldThrowIllegalArgumentExceptionWhenJobOfferIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createJobOfferService.create(null)
        );

        verifyNoInteractions(
                jobOfferRepository,
                jobOfferStatusHistoryRepository
        );
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUserIdIsNull() {
        JobOffer jobOffer = createJobOffer();
        jobOffer.setUserId(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> createJobOfferService.create(jobOffer)
        );

        verifyNoInteractions(
                jobOfferRepository,
                jobOfferStatusHistoryRepository
        );
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUrlIsNull() {
        JobOffer jobOffer = createJobOffer();
        jobOffer.setUrl(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> createJobOfferService.create(jobOffer)
        );

        verifyNoInteractions(
                jobOfferRepository,
                jobOfferStatusHistoryRepository
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldThrowIllegalArgumentExceptionWhenUrlIsBlank(String url) {
        JobOffer jobOffer = createJobOffer();
        jobOffer.setUrl(url);

        assertThrows(
                IllegalArgumentException.class,
                () -> createJobOfferService.create(jobOffer)
        );

        verifyNoInteractions(
                jobOfferRepository,
                jobOfferStatusHistoryRepository
        );
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSourceIsNull() {
        JobOffer jobOffer = createJobOffer();
        jobOffer.setSource(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> createJobOfferService.create(jobOffer)
        );

        verifyNoInteractions(
                jobOfferRepository,
                jobOfferStatusHistoryRepository
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldThrowIllegalArgumentExceptionWhenSourceIsBlank(String source) {
        JobOffer jobOffer = createJobOffer();
        jobOffer.setSource(source);

        assertThrows(
                IllegalArgumentException.class,
                () -> createJobOfferService.create(jobOffer)
        );

        verifyNoInteractions(
                jobOfferRepository,
                jobOfferStatusHistoryRepository
        );
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenExternalIdIsNull() {
        JobOffer jobOffer = createJobOffer();
        jobOffer.setExternalId(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> createJobOfferService.create(jobOffer)
        );

        verifyNoInteractions(
                jobOfferRepository,
                jobOfferStatusHistoryRepository
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldThrowIllegalArgumentExceptionWhenExternalIdIsBlank(
            String externalId
    ) {
        JobOffer jobOffer = createJobOffer();
        jobOffer.setExternalId(externalId);

        assertThrows(
                IllegalArgumentException.class,
                () -> createJobOfferService.create(jobOffer)
        );

        verifyNoInteractions(
                jobOfferRepository,
                jobOfferStatusHistoryRepository
        );
    }

    // ============================================================
    // CT-020 - Crear oferta registra estado inicial
    // ============================================================

    @Test
    void shouldRegisterInitialPendingStatusInHistoryWhenJobOfferIsCreated() {
        JobOffer jobOffer = createJobOffer();

        when(jobOfferRepository.existsByUserIdAndSourceAndExternalId(
                1L,
                "LinkedIn",
                "123"
        )).thenReturn(false);

        when(jobOfferRepository.save(jobOffer))
                .thenAnswer(invocation -> {
                    jobOffer.setId(1L);
                    return jobOffer;
                });

        createJobOfferService.create(jobOffer);

        verify(jobOfferStatusHistoryRepository).save(
                argThat(history ->
                        history.getJobOfferId().equals(1L)
                                && history.getStatus()
                                == JobOfferStatus.PENDIENTE
                                && history.getChangedAt() != null
                )
        );
    }

    private JobOffer createJobOffer() {
        JobOffer jobOffer = new JobOffer();

        jobOffer.setUserId(1L);
        jobOffer.setSource("LinkedIn");
        jobOffer.setExternalId("123");
        jobOffer.setUrl("https://linkedin.com/jobs/123");

        return jobOffer;
    }
}