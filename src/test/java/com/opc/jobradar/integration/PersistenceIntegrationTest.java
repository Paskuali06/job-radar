package com.opc.jobradar.integration;

import com.opc.jobradar.domain.model.UserRole;
import com.opc.jobradar.infrastructure.persistence.entity.JobOfferEntity;
import com.opc.jobradar.infrastructure.persistence.entity.UserEntity;
import com.opc.jobradar.infrastructure.persistence.repository.JobOfferJpaRepository;
import com.opc.jobradar.infrastructure.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class PersistenceIntegrationTest {

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private JobOfferJpaRepository jobOfferRepository;

    @Test
    void shouldPersistAndRetrieveUserFromPostgresql() {
        UserEntity user = createUser("ct033-user@example.com");

        UserEntity savedUser = userRepository.saveAndFlush(user);
        Optional<UserEntity> result = userRepository.findById(savedUser.getId());

        assertTrue(result.isPresent());
        assertEquals("ct033-user@example.com", result.get().getEmail());
        assertEquals(UserRole.USER, result.get().getRole());
    }

    @Test
    void shouldPersistAndRetrieveJobOfferFromPostgresql() {
        UserEntity user =
                userRepository.saveAndFlush(
                        createUser("ct033-offer@example.com")
                );

        JobOfferEntity offer =
                createJobOffer(
                        user.getId(),
                        "ct033-source",
                        "ct033-external-id"
                );

        JobOfferEntity savedOffer =
                jobOfferRepository.saveAndFlush(offer);

        Optional<JobOfferEntity> result =
                jobOfferRepository.findById(savedOffer.getId());

        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getUserId());
        assertEquals("ct033-source", result.get().getSource());
        assertEquals("ct033-external-id", result.get().getExternalId());
        assertNotNull(result.get().getCreatedAt());
        assertNotNull(result.get().getUpdatedAt());
    }

    @Test
    void shouldEnforceUserSourceAndExternalIdUniquenessInPostgresql() {
        UserEntity user =
                userRepository.saveAndFlush(
                        createUser("ct033-unique@example.com")
                );

        jobOfferRepository.saveAndFlush(
                createJobOffer(user.getId(), "LinkedIn", "same-id")
        );

        JobOfferEntity duplicate =
                createJobOffer(user.getId(), "LinkedIn", "same-id");

        assertThrows(
                DataIntegrityViolationException.class,
                () -> jobOfferRepository.saveAndFlush(duplicate)
        );
    }

    @Test
    void shouldAllowSameExternalIdForDifferentUsers() {
        UserEntity firstUser =
                userRepository.saveAndFlush(
                        createUser("ct033-first@example.com")
                );

        UserEntity secondUser =
                userRepository.saveAndFlush(
                        createUser("ct033-second@example.com")
                );

        jobOfferRepository.saveAndFlush(
                createJobOffer(firstUser.getId(), "LinkedIn", "same-id")
        );

        jobOfferRepository.saveAndFlush(
                createJobOffer(secondUser.getId(), "LinkedIn", "same-id")
        );

        assertEquals(2, jobOfferRepository.count());
    }

    @Test
    void shouldKeepJobOffersIsolatedByUser() {
        UserEntity firstUser =
                userRepository.saveAndFlush(
                        createUser("ct033-isolation-1@example.com")
                );

        UserEntity secondUser =
                userRepository.saveAndFlush(
                        createUser("ct033-isolation-2@example.com")
                );

        JobOfferEntity firstOffer =
                jobOfferRepository.saveAndFlush(
                        createJobOffer(
                                firstUser.getId(),
                                "LinkedIn",
                                "first-offer"
                        )
                );

        assertTrue(
                jobOfferRepository
                        .findByIdAndUserId(
                                firstOffer.getId(),
                                firstUser.getId()
                        )
                        .isPresent()
        );

        assertTrue(
                jobOfferRepository
                        .findByIdAndUserId(
                                firstOffer.getId(),
                                secondUser.getId()
                        )
                        .isEmpty()
        );
    }

    private UserEntity createUser(String email) {
        UserEntity user = new UserEntity();
        user.setName("CT033 Test User");
        user.setEmail(email);
        user.setPassword("test-password");
        user.setRole(UserRole.USER);
        return user;
    }

    private JobOfferEntity createJobOffer(
            Long userId,
            String source,
            String externalId
    ) {
        OffsetDateTime now = OffsetDateTime.now();

        JobOfferEntity offer = new JobOfferEntity();
        offer.setUserId(userId);
        offer.setCompany("CT033 Company");
        offer.setTitle("CT033 Developer");
        offer.setLocation("Madrid");
        offer.setWorkMode("REMOTO");
        offer.setStatus("PENDIENTE");
        offer.setUrl("https://example.com/" + externalId);
        offer.setSource(source);
        offer.setExternalId(externalId);
        offer.setCreatedAt(now);
        offer.setUpdatedAt(now);

        return offer;
    }
}