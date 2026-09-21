package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferAlreadyExistsException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class IngestJobOfferServiceTest {

    private static final Long USER_ID = 1L;

    private final JobOfferRepository repository =
            mock(JobOfferRepository.class);

    private final IngestJobOfferService service =
            new IngestJobOfferService(repository);

    @Test
    void shouldIngestExternalJobOffer() {
        ExternalJobOffer externalJobOffer = validExternalJobOffer();

        when(repository.existsByUserIdAndSourceAndExternalId(
                USER_ID,
                "LINKEDIN",
                "123"
        )).thenReturn(false);

        JobOffer savedJobOffer = new JobOffer(
                10L,
                "Google",
                "Java Developer",
                "Madrid",
                "REMOTO",
                "https://example.com/job/123",
                externalJobOffer.publishedAt(),
                JobOfferStatus.PENDIENTE,
                "Desarrollo de aplicaciones Java",
                null,
                null,
                "LINKEDIN",
                "123"
        );

        savedJobOffer.setUserId(USER_ID);

        when(repository.save(any(JobOffer.class)))
                .thenReturn(savedJobOffer);

        JobOffer result = service.ingest(USER_ID, externalJobOffer);

        assertEquals(USER_ID, result.getUserId());
        assertEquals("Google", result.getCompany());
        assertEquals("Java Developer", result.getTitle());
        assertEquals("Madrid", result.getLocation());
        assertEquals("REMOTO", result.getWorkMode());
        assertEquals(
                "https://example.com/job/123",
                result.getUrl()
        );
        assertEquals(
                externalJobOffer.publishedAt(),
                result.getPublishedAt()
        );
        assertEquals(
                JobOfferStatus.PENDIENTE,
                result.getStatus()
        );
        assertEquals(
                "Desarrollo de aplicaciones Java",
                result.getDescription()
        );
        assertEquals("LINKEDIN", result.getSource());
        assertEquals("123", result.getExternalId());

        verify(repository).save(any(JobOffer.class));
    }

    @Test
    void shouldRejectDuplicateExternalJobOffer() {
        ExternalJobOffer externalJobOffer = validExternalJobOffer();

        when(repository.existsByUserIdAndSourceAndExternalId(
                USER_ID,
                "LINKEDIN",
                "123"
        )).thenReturn(true);

        assertThrows(
                JobOfferAlreadyExistsException.class,
                () -> service.ingest(USER_ID, externalJobOffer)
        );

        verify(repository, never()).save(any(JobOffer.class));
    }

    @Test
    void shouldAllowSameExternalOfferForDifferentUsers() {
        Long otherUserId = 2L;

        ExternalJobOffer externalJobOffer = validExternalJobOffer();

        when(repository.existsByUserIdAndSourceAndExternalId(
                otherUserId,
                "LINKEDIN",
                "123"
        )).thenReturn(false);

        JobOffer savedJobOffer = new JobOffer(
                20L,
                "Google",
                "Java Developer",
                "Madrid",
                "REMOTO",
                "https://example.com/job/123",
                externalJobOffer.publishedAt(),
                JobOfferStatus.PENDIENTE,
                "Desarrollo de aplicaciones Java",
                null,
                null,
                "LINKEDIN",
                "123"
        );

        savedJobOffer.setUserId(otherUserId);

        when(repository.save(any(JobOffer.class)))
                .thenReturn(savedJobOffer);

        JobOffer result = service.ingest(
                otherUserId,
                externalJobOffer
        );

        assertEquals(otherUserId, result.getUserId());
        assertEquals("LINKEDIN", result.getSource());
        assertEquals("123", result.getExternalId());

        verify(repository).save(any(JobOffer.class));
    }

    @Test
    void shouldRejectEmptySource() {
        ExternalJobOffer externalJobOffer = new ExternalJobOffer(
                "",
                "123",
                "Google",
                "Java Developer",
                "Madrid",
                "REMOTO",
                "https://example.com/job/123",
                "Desarrollo de aplicaciones Java",
                publishedAt()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.ingest(USER_ID, externalJobOffer)
        );

        verifyNoRepositoryInteraction();
    }

    @Test
    void shouldRejectEmptyExternalId() {
        ExternalJobOffer externalJobOffer = new ExternalJobOffer(
                "LINKEDIN",
                "",
                "Google",
                "Java Developer",
                "Madrid",
                "REMOTO",
                "https://example.com/job/123",
                "Desarrollo de aplicaciones Java",
                publishedAt()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.ingest(USER_ID, externalJobOffer)
        );

        verifyNoRepositoryInteraction();
    }

    @Test
    void shouldRejectEmptyCompany() {
        ExternalJobOffer externalJobOffer = new ExternalJobOffer(
                "LINKEDIN",
                "123",
                "",
                "Java Developer",
                "Madrid",
                "REMOTO",
                "https://example.com/job/123",
                "Desarrollo de aplicaciones Java",
                publishedAt()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.ingest(USER_ID, externalJobOffer)
        );

        verifyNoRepositoryInteraction();
    }

    @Test
    void shouldRejectEmptyTitle() {
        ExternalJobOffer externalJobOffer = new ExternalJobOffer(
                "LINKEDIN",
                "123",
                "Google",
                "",
                "Madrid",
                "REMOTO",
                "https://example.com/job/123",
                "Desarrollo de aplicaciones Java",
                publishedAt()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.ingest(USER_ID, externalJobOffer)
        );

        verifyNoRepositoryInteraction();
    }

    @Test
    void shouldRejectEmptyUrl() {
        ExternalJobOffer externalJobOffer = new ExternalJobOffer(
                "LINKEDIN",
                "123",
                "Google",
                "Java Developer",
                "Madrid",
                "REMOTO",
                "",
                "Desarrollo de aplicaciones Java",
                publishedAt()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.ingest(USER_ID, externalJobOffer)
        );

        verifyNoRepositoryInteraction();
    }

    @Test
    void shouldRejectNullPublishedAt() {
        ExternalJobOffer externalJobOffer = new ExternalJobOffer(
                "LINKEDIN",
                "123",
                "Google",
                "Java Developer",
                "Madrid",
                "REMOTO",
                "https://example.com/job/123",
                "Desarrollo de aplicaciones Java",
                null
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.ingest(USER_ID, externalJobOffer)
        );

        verifyNoRepositoryInteraction();
    }

    @Test
    void shouldAllowOptionalFieldsToBeNull() {
        ExternalJobOffer externalJobOffer = new ExternalJobOffer(
                "LINKEDIN",
                "123",
                "Google",
                "Java Developer",
                null,
                null,
                "https://example.com/job/123",
                null,
                publishedAt()
        );

        when(repository.existsByUserIdAndSourceAndExternalId(
                USER_ID,
                "LINKEDIN",
                "123"
        )).thenReturn(false);

        JobOffer savedJobOffer = new JobOffer(
                30L,
                "Google",
                "Java Developer",
                null,
                null,
                "https://example.com/job/123",
                externalJobOffer.publishedAt(),
                JobOfferStatus.PENDIENTE,
                null,
                null,
                null,
                "LINKEDIN",
                "123"
        );

        savedJobOffer.setUserId(USER_ID);

        when(repository.save(any(JobOffer.class)))
                .thenReturn(savedJobOffer);

        JobOffer result = service.ingest(
                USER_ID,
                externalJobOffer
        );

        assertEquals(USER_ID, result.getUserId());
        assertEquals("Google", result.getCompany());
        assertEquals("Java Developer", result.getTitle());
        assertEquals(null, result.getLocation());
        assertEquals(null, result.getWorkMode());
        assertEquals(null, result.getDescription());

        verify(repository).save(any(JobOffer.class));
    }

    @Test
    void shouldNormalizeExternalOfferData() {
        ExternalJobOffer externalJobOffer = new ExternalJobOffer(
                " linkedin ",
                " 123 ",
                " Google ",
                " Java Developer ",
                " Madrid ",
                " remoto ",
                " https://example.com/job/123 ",
                " Desarrollo de aplicaciones Java ",
                publishedAt()
        );

        when(repository.existsByUserIdAndSourceAndExternalId(
                USER_ID,
                "LINKEDIN",
                "123"
        )).thenReturn(false);

        JobOffer savedJobOffer = new JobOffer(
                40L,
                "Google",
                "Java Developer",
                "Madrid",
                "REMOTO",
                "https://example.com/job/123",
                externalJobOffer.publishedAt(),
                JobOfferStatus.PENDIENTE,
                "Desarrollo de aplicaciones Java",
                null,
                null,
                "LINKEDIN",
                "123"
        );

        savedJobOffer.setUserId(USER_ID);

        when(repository.save(any(JobOffer.class)))
                .thenReturn(savedJobOffer);

        JobOffer result = service.ingest(
                USER_ID,
                externalJobOffer
        );

        assertEquals("LINKEDIN", result.getSource());
        assertEquals("123", result.getExternalId());
        assertEquals("Google", result.getCompany());
        assertEquals("Java Developer", result.getTitle());
        assertEquals("Madrid", result.getLocation());
        assertEquals("REMOTO", result.getWorkMode());
        assertEquals(
                "https://example.com/job/123",
                result.getUrl()
        );
        assertEquals(
                "Desarrollo de aplicaciones Java",
                result.getDescription()
        );

        verify(repository).existsByUserIdAndSourceAndExternalId(
                USER_ID,
                "LINKEDIN",
                "123"
        );
        verify(repository).save(any(JobOffer.class));
    }

    @Test
    void shouldNormalizeSourceAndExternalIdBeforeCheckingDuplicate() {
        ExternalJobOffer externalJobOffer = new ExternalJobOffer(
                " linkedin ",
                " 123 ",
                "Google",
                "Java Developer",
                "Madrid",
                "REMOTO",
                "https://example.com/job/123",
                "Desarrollo de aplicaciones Java",
                publishedAt()
        );

        when(repository.existsByUserIdAndSourceAndExternalId(
                USER_ID,
                "LINKEDIN",
                "123"
        )).thenReturn(true);

        assertThrows(
                JobOfferAlreadyExistsException.class,
                () -> service.ingest(
                        USER_ID,
                        externalJobOffer
                )
        );

        verify(repository).existsByUserIdAndSourceAndExternalId(
                USER_ID,
                "LINKEDIN",
                "123"
        );

        verify(repository, never()).save(any(JobOffer.class));
    }

    @Test
    void shouldKeepOptionalNullFieldsAsNullWhenNormalizing() {
        ExternalJobOffer externalJobOffer = new ExternalJobOffer(
                " linkedin ",
                " 123 ",
                " Google ",
                " Java Developer ",
                null,
                null,
                " https://example.com/job/123 ",
                null,
                publishedAt()
        );

        when(repository.existsByUserIdAndSourceAndExternalId(
                USER_ID,
                "LINKEDIN",
                "123"
        )).thenReturn(false);

        JobOffer savedJobOffer = new JobOffer(
                50L,
                "Google",
                "Java Developer",
                null,
                null,
                "https://example.com/job/123",
                externalJobOffer.publishedAt(),
                JobOfferStatus.PENDIENTE,
                null,
                null,
                null,
                "LINKEDIN",
                "123"
        );

        savedJobOffer.setUserId(USER_ID);

        when(repository.save(any(JobOffer.class)))
                .thenReturn(savedJobOffer);

        JobOffer result = service.ingest(
                USER_ID,
                externalJobOffer
        );

        assertEquals("LINKEDIN", result.getSource());
        assertEquals("123", result.getExternalId());
        assertEquals("Google", result.getCompany());
        assertEquals("Java Developer", result.getTitle());
        assertEquals(null, result.getLocation());
        assertEquals(null, result.getWorkMode());
        assertEquals(
                "https://example.com/job/123",
                result.getUrl()
        );
        assertEquals(null, result.getDescription());

        verify(repository).save(any(JobOffer.class));
    }

    private ExternalJobOffer validExternalJobOffer() {
        return new ExternalJobOffer(
                "LINKEDIN",
                "123",
                "Google",
                "Java Developer",
                "Madrid",
                "REMOTO",
                "https://example.com/job/123",
                "Desarrollo de aplicaciones Java",
                publishedAt()
        );
    }

    private OffsetDateTime publishedAt() {
        return OffsetDateTime.of(
                2026,
                9,
                20,
                10,
                0,
                0,
                0,
                ZoneOffset.UTC
        );
    }

    private void verifyNoRepositoryInteraction() {
        verifyNoInteractions(repository);
    }

    @Test
    void shouldIngestMultipleExternalJobOffers() {
    ExternalJobOffer firstOffer = new ExternalJobOffer(
            "LINKEDIN",
            "123",
            "Google",
            "Java Developer",
            "Madrid",
            "REMOTO",
            "https://example.com/job/123",
            "Desarrollo Java",
            publishedAt()
    );

    ExternalJobOffer secondOffer = new ExternalJobOffer(
            "INFOJOBS",
            "456",
            "Amazon",
            "Backend Developer",
            "Barcelona",
            "HIBRIDO",
            "https://example.com/job/456",
            "Desarrollo backend",
            publishedAt()
    );

    when(repository.existsByUserIdAndSourceAndExternalId(
            USER_ID,
            "LINKEDIN",
            "123"
    )).thenReturn(false);

    when(repository.existsByUserIdAndSourceAndExternalId(
            USER_ID,
            "INFOJOBS",
            "456"
    )).thenReturn(false);

    JobOffer firstSaved = new JobOffer(
            60L,
            "Google",
            "Java Developer",
            "Madrid",
            "REMOTO",
            "https://example.com/job/123",
            firstOffer.publishedAt(),
            JobOfferStatus.PENDIENTE,
            "Desarrollo Java",
            null,
            null,
            "LINKEDIN",
            "123"
    );
    firstSaved.setUserId(USER_ID);

    JobOffer secondSaved = new JobOffer(
            61L,
            "Amazon",
            "Backend Developer",
            "Barcelona",
            "HIBRIDO",
            "https://example.com/job/456",
            secondOffer.publishedAt(),
            JobOfferStatus.PENDIENTE,
            "Desarrollo backend",
            null,
            null,
            "INFOJOBS",
            "456"
    );
    secondSaved.setUserId(USER_ID);

    when(repository.save(any(JobOffer.class)))
            .thenReturn(firstSaved)
            .thenReturn(secondSaved);

    List<JobOffer> result = service.ingestAll(
            USER_ID,
            List.of(firstOffer, secondOffer)
    );

    assertEquals(2, result.size());
    assertIterableEquals(
            List.of(firstSaved, secondSaved),
            result
    );
}

    @Test
    void shouldKeepIngestedOffersInOriginalOrder() {
    ExternalJobOffer firstOffer = new ExternalJobOffer(
            "LINKEDIN",
            "123",
            "Google",
            "Java Developer",
            "Madrid",
            "REMOTO",
            "https://example.com/job/123",
            "Desarrollo Java",
            publishedAt()
    );

    ExternalJobOffer secondOffer = new ExternalJobOffer(
            "INFOJOBS",
            "456",
            "Amazon",
            "Backend Developer",
            "Barcelona",
            "HIBRIDO",
            "https://example.com/job/456",
            "Desarrollo backend",
            publishedAt()
    );

    when(repository.existsByUserIdAndSourceAndExternalId(
            USER_ID,
            "LINKEDIN",
            "123"
    )).thenReturn(false);

    when(repository.existsByUserIdAndSourceAndExternalId(
            USER_ID,
            "INFOJOBS",
            "456"
    )).thenReturn(false);

    JobOffer firstSaved = new JobOffer(
            62L,
            "Google",
            "Java Developer",
            "Madrid",
            "REMOTO",
            "https://example.com/job/123",
            firstOffer.publishedAt(),
            JobOfferStatus.PENDIENTE,
            "Desarrollo Java",
            null,
            null,
            "LINKEDIN",
            "123"
    );
    firstSaved.setUserId(USER_ID);

    JobOffer secondSaved = new JobOffer(
            63L,
            "Amazon",
            "Backend Developer",
            "Barcelona",
            "HIBRIDO",
            "https://example.com/job/456",
            secondOffer.publishedAt(),
            JobOfferStatus.PENDIENTE,
            "Desarrollo backend",
            null,
            null,
            "INFOJOBS",
            "456"
    );
    secondSaved.setUserId(USER_ID);

    when(repository.save(any(JobOffer.class)))
            .thenReturn(firstSaved)
            .thenReturn(secondSaved);

    List<JobOffer> result = service.ingestAll(
            USER_ID,
            List.of(firstOffer, secondOffer)
    );

    assertEquals(firstSaved, result.get(0));
    assertEquals(secondSaved, result.get(1));
}

    @Test
    void shouldContinueAfterDuplicateOffer() {
    ExternalJobOffer duplicateOffer = validExternalJobOffer();

    ExternalJobOffer validOffer = new ExternalJobOffer(
            "INFOJOBS",
            "456",
            "Amazon",
            "Backend Developer",
            "Barcelona",
            "HIBRIDO",
            "https://example.com/job/456",
            "Desarrollo backend",
            publishedAt()
    );

    when(repository.existsByUserIdAndSourceAndExternalId(
            USER_ID,
            "LINKEDIN",
            "123"
    )).thenReturn(true);

    when(repository.existsByUserIdAndSourceAndExternalId(
            USER_ID,
            "INFOJOBS",
            "456"
    )).thenReturn(false);

    JobOffer savedOffer = new JobOffer(
            64L,
            "Amazon",
            "Backend Developer",
            "Barcelona",
            "HIBRIDO",
            "https://example.com/job/456",
            validOffer.publishedAt(),
            JobOfferStatus.PENDIENTE,
            "Desarrollo backend",
            null,
            null,
            "INFOJOBS",
            "456"
    );
    savedOffer.setUserId(USER_ID);

    when(repository.save(any(JobOffer.class)))
            .thenReturn(savedOffer);

    List<JobOffer> result = service.ingestAll(
            USER_ID,
            List.of(duplicateOffer, validOffer)
    );

    assertEquals(1, result.size());
    assertEquals(savedOffer, result.get(0));

    verify(repository).save(any(JobOffer.class));
}

    @Test
    void shouldContinueAfterInvalidOffer() {
    ExternalJobOffer invalidOffer = new ExternalJobOffer(
            "",
            "123",
            "Google",
            "Java Developer",
            "Madrid",
            "REMOTO",
            "https://example.com/job/123",
            "Desarrollo Java",
            publishedAt()
    );

    ExternalJobOffer validOffer = validExternalJobOffer();

    when(repository.existsByUserIdAndSourceAndExternalId(
            USER_ID,
            "LINKEDIN",
            "123"
    )).thenReturn(false);

    JobOffer savedOffer = new JobOffer(
            65L,
            "Google",
            "Java Developer",
            "Madrid",
            "REMOTO",
            "https://example.com/job/123",
            validOffer.publishedAt(),
            JobOfferStatus.PENDIENTE,
            "Desarrollo Java",
            null,
            null,
            "LINKEDIN",
            "123"
    );
    savedOffer.setUserId(USER_ID);

    when(repository.save(any(JobOffer.class)))
            .thenReturn(savedOffer);

    List<JobOffer> result = service.ingestAll(
            USER_ID,
            List.of(invalidOffer, validOffer)
    );

    assertEquals(1, result.size());
    assertEquals(savedOffer, result.get(0));

    verify(repository).save(any(JobOffer.class));
}

    @Test
    void shouldReturnEmptyListWhenThereAreNoExternalJobOffers() {
    List<JobOffer> result = service.ingestAll(
            USER_ID,
            List.of()
    );

    assertEquals(List.of(), result);
    verifyNoInteractions(repository);
}

    @Test
    void shouldReturnEmptyListWhenAllOffersAreRejected() {
    ExternalJobOffer invalidOffer = new ExternalJobOffer(
            "",
            "123",
            "Google",
            "Java Developer",
            "Madrid",
            "REMOTO",
            "https://example.com/job/123",
            "Desarrollo Java",
            publishedAt()
    );

    ExternalJobOffer duplicateOffer = validExternalJobOffer();

    when(repository.existsByUserIdAndSourceAndExternalId(
            USER_ID,
            "LINKEDIN",
            "123"
    )).thenReturn(true);

    List<JobOffer> result = service.ingestAll(
            USER_ID,
            List.of(invalidOffer, duplicateOffer)
    );

    assertEquals(List.of(), result);
    verify(repository, never()).save(any(JobOffer.class));
}
}