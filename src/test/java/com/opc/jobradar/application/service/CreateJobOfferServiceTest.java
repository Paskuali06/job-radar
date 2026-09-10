package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferAlreadyExistsException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CreateJobOfferServiceTest {

    private final JobOfferRepository jobOfferRepository =
            mock(JobOfferRepository.class);

    private final CreateJobOfferService createJobOfferService =
            new CreateJobOfferService(jobOfferRepository);

    // ============================================================
    // CT-001 - Crear oferta cuando la identidad no existe
    // ============================================================

    @Test
    void shouldCreateJobOfferWhenSourceAndExternalIdDoNotExist() {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setSource("LinkedIn");
        jobOffer.setExternalId("123");
        jobOffer.setUrl("https://linkedin.com/jobs/123");

        when(jobOfferRepository.existsBySourceAndExternalId(
                "LinkedIn",
                "123"
        )).thenReturn(false);

        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        JobOffer result = createJobOfferService.create(jobOffer);

        assertNotNull(result);
        assertSame(jobOffer, result);

        verify(jobOfferRepository).existsBySourceAndExternalId(
                "LinkedIn",
                "123"
        );

        verify(jobOfferRepository).save(jobOffer);
    }

    // ============================================================
    // CT-008 - Una oferta nueva sin estado comienza en PENDIENTE
    // ============================================================

    @Test
    void shouldAssignPendingStatusWhenCreatingJobOfferWithoutStatus() {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setSource("LinkedIn");
        jobOffer.setExternalId("123");
        jobOffer.setUrl("https://linkedin.com/jobs/123");

        when(jobOfferRepository.existsBySourceAndExternalId(
                "LinkedIn",
                "123"
        )).thenReturn(false);

        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        JobOffer result = createJobOfferService.create(jobOffer);

        assertSame(jobOffer, result);
        assertEquals(JobOfferStatus.PENDIENTE, result.getStatus());

        verify(jobOfferRepository).existsBySourceAndExternalId(
                "LinkedIn",
                "123"
        );

        verify(jobOfferRepository).save(jobOffer);
    }

    // ============================================================
    // CT-002 - Rechazar oferta si la identidad ya existe
    // ============================================================

    @Test
    void shouldRejectJobOfferWhenSourceAndExternalIdAlreadyExist() {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setSource("LinkedIn");
        jobOffer.setExternalId("123");
        jobOffer.setUrl("https://linkedin.com/jobs/123");

        when(jobOfferRepository.existsBySourceAndExternalId(
                "LinkedIn",
                "123"
        )).thenReturn(true);

        JobOfferAlreadyExistsException exception = assertThrows(
                JobOfferAlreadyExistsException.class,
                () -> createJobOfferService.create(jobOffer)
        );

        assertEquals(
                "Esta oferta ya existe",
                exception.getMessage()
        );

        verify(jobOfferRepository).existsBySourceAndExternalId(
                "LinkedIn",
                "123"
        );

        verify(jobOfferRepository, never()).save(any());
    }

    // ============================================================
    // CT-003 - Rechazar oferta si la URL es null
    // ============================================================

    @Test
    void shouldRejectJobOfferWhenUrlIsNull() {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setSource("LinkedIn");
        jobOffer.setExternalId("123");
        jobOffer.setUrl(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createJobOfferService.create(jobOffer)
        );

        assertEquals(
                "Job offer URL cannot be null or blank.",
                exception.getMessage()
        );

        verifyNoInteractions(jobOfferRepository);
    }

    // ============================================================
    // CT-004 - Rechazar oferta si la URL está vacía o en blanco
    // ============================================================

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    "})
    void shouldRejectJobOfferWhenUrlIsBlank(String url) {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setSource("LinkedIn");
        jobOffer.setExternalId("123");
        jobOffer.setUrl(url);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createJobOfferService.create(jobOffer)
        );

        assertEquals(
                "Job offer URL cannot be null or blank.",
                exception.getMessage()
        );

        verifyNoInteractions(jobOfferRepository);
    }

    // ============================================================
    // CT-005 - Devolver la oferta guardada
    // ============================================================

    @Test
    void shouldReturnSavedJobOffer() {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setSource("LinkedIn");
        jobOffer.setExternalId("123");
        jobOffer.setUrl("https://linkedin.com/jobs/123");

        JobOffer savedJobOffer = new JobOffer();
        savedJobOffer.setSource("LinkedIn");
        savedJobOffer.setExternalId("123");
        savedJobOffer.setUrl("https://linkedin.com/jobs/123");

        when(jobOfferRepository.existsBySourceAndExternalId(
                "LinkedIn",
                "123"
        )).thenReturn(false);

        when(jobOfferRepository.save(jobOffer))
                .thenReturn(savedJobOffer);

        JobOffer result = createJobOfferService.create(jobOffer);

        assertSame(savedJobOffer, result);

        verify(jobOfferRepository).existsBySourceAndExternalId(
                "LinkedIn",
                "123"
        );

        verify(jobOfferRepository).save(jobOffer);
    }

    // ============================================================
    // CT-006 - Mismo externalId pero diferente source
    // ============================================================

    @Test
    void shouldCreateJobOfferWhenSourceIsDifferent() {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setSource("Indeed");
        jobOffer.setExternalId("123");
        jobOffer.setUrl("https://indeed.com/jobs/123");

        when(jobOfferRepository.existsBySourceAndExternalId(
                "Indeed",
                "123"
        )).thenReturn(false);

        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        JobOffer result = createJobOfferService.create(jobOffer);

        assertNotNull(result);
        assertSame(jobOffer, result);

        verify(jobOfferRepository).existsBySourceAndExternalId(
                "Indeed",
                "123"
        );

        verify(jobOfferRepository).save(jobOffer);
    }

    // ============================================================
    // CT-006 - Mismo source pero diferente externalId
    // ============================================================

    @Test
    void shouldCreateJobOfferWhenExternalIdIsDifferent() {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setSource("LinkedIn");
        jobOffer.setExternalId("456");
        jobOffer.setUrl("https://linkedin.com/jobs/456");

        when(jobOfferRepository.existsBySourceAndExternalId(
                "LinkedIn",
                "456"
        )).thenReturn(false);

        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        JobOffer result = createJobOfferService.create(jobOffer);

        assertNotNull(result);
        assertSame(jobOffer, result);

        verify(jobOfferRepository).existsBySourceAndExternalId(
                "LinkedIn",
                "456"
        );

        verify(jobOfferRepository).save(jobOffer);
    }

    // ============================================================
    // CT-006 - Source obligatorio
    // ============================================================

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    "})
    void shouldRejectJobOfferWhenSourceIsBlank(String source) {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setSource(source);
        jobOffer.setExternalId("123");
        jobOffer.setUrl("https://linkedin.com/jobs/123");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createJobOfferService.create(jobOffer)
        );

        assertEquals(
                "Job offer source cannot be null or blank.",
                exception.getMessage()
        );

        verifyNoInteractions(jobOfferRepository);
    }

    // ============================================================
    // CT-006 - ExternalId obligatorio
    // ============================================================

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    "})
    void shouldRejectJobOfferWhenExternalIdIsBlank(String externalId) {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setSource("LinkedIn");
        jobOffer.setExternalId(externalId);
        jobOffer.setUrl("https://linkedin.com/jobs/123");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createJobOfferService.create(jobOffer)
        );

        assertEquals(
                "Job offer external ID cannot be null or blank.",
                exception.getMessage()
        );

        verifyNoInteractions(jobOfferRepository);
    }

    // ============================================================
    // CT-006 - La URL no determina la identidad
    // ============================================================

    @Test
    void shouldRejectJobOfferWhenIdentityExistsWithDifferentUrl() {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setSource("LinkedIn");
        jobOffer.setExternalId("123");
        jobOffer.setUrl("https://linkedin.com/jobs/123?tracking=abc");

        when(jobOfferRepository.existsBySourceAndExternalId(
                "LinkedIn",
                "123"
        )).thenReturn(true);

        JobOfferAlreadyExistsException exception = assertThrows(
                JobOfferAlreadyExistsException.class,
                () -> createJobOfferService.create(jobOffer)
        );

        assertEquals(
                "Esta oferta ya existe",
                exception.getMessage()
        );

        verify(jobOfferRepository).existsBySourceAndExternalId(
                "LinkedIn",
                "123"
        );

        verify(jobOfferRepository, never())
                .existsByUrl(anyString());

        verify(jobOfferRepository, never()).save(any());
    }

    // ============================================================
    // CT-016 - Rechazar oferta duplicada
    // ============================================================

    @Test
    void shouldThrowExceptionWhenJobOfferAlreadyExists() {
        JobOfferRepository repository = mock(JobOfferRepository.class);

        JobOffer jobOffer = new JobOffer();
        jobOffer.setUrl("https://example.com/job");
        jobOffer.setSource("LINKEDIN");
        jobOffer.setExternalId("123");

        when(repository.existsBySourceAndExternalId(
                "LINKEDIN",
                "123"
        )).thenReturn(true);

        CreateJobOfferService service =
                new CreateJobOfferService(repository);

        assertThrows(
                JobOfferAlreadyExistsException.class,
                () -> service.create(jobOffer)
        );

        verify(repository, never()).save(jobOffer);
    }
}