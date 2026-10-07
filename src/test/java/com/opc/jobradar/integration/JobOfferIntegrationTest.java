package com.opc.jobradar.integration;

import com.opc.jobradar.application.service.CreateJobOfferService;
import com.opc.jobradar.application.service.GetJobOfferService;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.domain.model.UserRole;
import com.opc.jobradar.infrastructure.persistence.entity.JobOfferEntity;
import com.opc.jobradar.infrastructure.persistence.entity.UserEntity;
import com.opc.jobradar.infrastructure.persistence.repository.JobOfferJpaRepository;
import com.opc.jobradar.infrastructure.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class JobOfferIntegrationTest {

    @Autowired
    private CreateJobOfferService createJobOfferService;

    @Autowired
    private GetJobOfferService getJobOfferService;

    @Autowired
    private JobOfferJpaRepository jobOfferJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    private Long userId;

    @BeforeEach
    void setUp() {
        UserEntity user = new UserEntity();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(UserRole.USER);

        UserEntity savedUser = userJpaRepository.saveAndFlush(user);
        userId = savedUser.getId();
    }

    @Test
    void shouldPersistAndRetrieveJobOffer() {
        JobOffer jobOffer = createJobOffer(userId);

        JobOffer savedJobOffer = createJobOfferService.create(jobOffer);

        JobOffer retrievedJobOffer =
                getJobOfferService.getById(savedJobOffer.getId(), userId);

        assertEquals(savedJobOffer.getId(), retrievedJobOffer.getId());
        assertEquals(userId, retrievedJobOffer.getUserId());
        assertEquals("Empresa Integration", retrievedJobOffer.getCompany());
        assertEquals("Backend Developer", retrievedJobOffer.getTitle());
        assertEquals("Madrid", retrievedJobOffer.getLocation());
        assertEquals("REMOTO", retrievedJobOffer.getWorkMode());
        assertEquals(
                "https://example.com/integration-job",
                retrievedJobOffer.getUrl()
        );
        assertEquals("LinkedIn", retrievedJobOffer.getSource());
        assertEquals("integration-001", retrievedJobOffer.getExternalId());
        assertEquals(JobOfferStatus.PENDIENTE, retrievedJobOffer.getStatus());
    }

    @Test
    void shouldEnforceUniqueSourceAndExternalIdForSameUser() {
        JobOfferEntity firstOffer = createEntity(
                userId,
                "LinkedIn",
                "integration-unique-001"
        );

        JobOfferEntity duplicateOffer = createEntity(
                userId,
                "LinkedIn",
                "integration-unique-001"
        );

        jobOfferJpaRepository.saveAndFlush(firstOffer);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> jobOfferJpaRepository.saveAndFlush(duplicateOffer)
        );
    }

    private JobOffer createJobOffer(Long userId) {
        OffsetDateTime now = OffsetDateTime.now();

        JobOffer jobOffer = new JobOffer();

        jobOffer.setUserId(userId);
        jobOffer.setCompany("Empresa Integration");
        jobOffer.setTitle("Backend Developer");
        jobOffer.setLocation("Madrid");
        jobOffer.setWorkMode("REMOTO");
        jobOffer.setUrl("https://example.com/integration-job");
        jobOffer.setSource("LinkedIn");
        jobOffer.setExternalId("integration-001");
        jobOffer.setCreatedAt(now);
        jobOffer.setUpdatedAt(now);

        return jobOffer;
    }

    private JobOfferEntity createEntity(
            Long userId,
            String source,
            String externalId
    ) {
        OffsetDateTime now = OffsetDateTime.now();

        JobOfferEntity entity = new JobOfferEntity();

        entity.setUserId(userId);
        entity.setCompany("Empresa Integration");
        entity.setTitle("Backend Developer");
        entity.setLocation("Madrid");
        entity.setWorkMode("REMOTO");
        entity.setUrl("https://example.com/" + externalId);
        entity.setSource(source);
        entity.setExternalId(externalId);
        entity.setStatus("PENDIENTE");
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return entity;
    }
    @Test
    void shouldPersistJobOfferWithLongExternalId() {
    JobOffer jobOffer = createJobOffer(userId);

    String longExternalId =
            "https://empleo.metrica-global.com/desarrollador-a-backend-java-microservicios-1jhqpLCORw5r/?utm_source=linkedin&utm_medium=job_board&src=linkedin";

    jobOffer.setExternalId(longExternalId);

    JobOffer savedJobOffer =
            createJobOfferService.create(jobOffer);

    assertEquals(
            longExternalId,
            savedJobOffer.getExternalId()
    );
}
}