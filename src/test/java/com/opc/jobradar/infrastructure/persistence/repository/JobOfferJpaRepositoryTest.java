package com.opc.jobradar.infrastructure.persistence.repository;


import com.opc.jobradar.infrastructure.persistence.entity.JobOfferEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class JobOfferJpaRepositoryTest {

    @Autowired
    private JobOfferJpaRepository repository;

    @Test
    void shouldReturnAllJobOffersWhenNoFiltersAreProvided() {
        JobOfferEntity firstOffer = createJobOffer(
                "Empresa A",
                "Backend Developer",
                "Madrid",
                "REMOTO",
                "PENDIENTE",
                "https://example.com/job-a",
                "LinkedIn",
                "job-a"
        );

        JobOfferEntity secondOffer = createJobOffer(
                "Empresa B",
                "Java Developer",
                "Barcelona",
                "HIBRIDO",
                "SOLICITADA",
                "https://example.com/job-b",
                "Indeed",
                "job-b"
        );

        repository.save(firstOffer);
        repository.save(secondOffer);

        List<JobOfferEntity> result = repository.findAll(
                null,
                null,
                null,
                null
        );

        assertEquals(2, result.size());
    }

    private JobOfferEntity createJobOffer(
            String company,
            String title,
            String location,
            String workMode,
            String status,
            String url,
            String source,
            String externalId
    ) {
        OffsetDateTime now = OffsetDateTime.now();

        JobOfferEntity entity = new JobOfferEntity();

        entity.setCompany(company);
        entity.setTitle(title);
        entity.setLocation(location);
        entity.setWorkMode(workMode);
        entity.setStatus(status);
        entity.setUrl(url);
        entity.setSource(source);
        entity.setExternalId(externalId);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return entity;
    }
    @Test
    void shouldFilterByLocationAndWorkMode() {
    JobOfferEntity firstOffer = createJobOffer(
            "Empresa A",
            "Backend Developer",
            "Madrid",
            "REMOTO",
            "PENDIENTE",
            "https://example.com/job-a",
            "LinkedIn",
            "job-a"
    );

    JobOfferEntity secondOffer = createJobOffer(
            "Empresa B",
            "Java Developer",
            "Madrid",
            "HIBRIDO",
            "SOLICITADA",
            "https://example.com/job-b",
            "Indeed",
            "job-b"
    );

    JobOfferEntity thirdOffer = createJobOffer(
            "Empresa C",
            "Java Developer",
            "Barcelona",
            "REMOTO",
            "SOLICITADA",
            "https://example.com/job-c",
            "InfoJobs",
            "job-c"
    );

    repository.save(firstOffer);
    repository.save(secondOffer);
    repository.save(thirdOffer);

    List<JobOfferEntity> result = repository.findAll(
            null,
            "Madrid",
            "REMOTO",
            null
    );

    assertEquals(1, result.size());
    assertEquals("Empresa A", result.get(0).getCompany());
}

    @Test
    void shouldFilterByCompany() {
    JobOfferEntity firstOffer = createJobOffer(
            "Empresa A",
            "Backend Developer",
            "Madrid",
            "REMOTO",
            "PENDIENTE",
            "https://example.com/job-a",
            "LinkedIn",
            "job-a"
    );

    JobOfferEntity secondOffer = createJobOffer(
            "Empresa B",
            "Java Developer",
            "Madrid",
            "HIBRIDO",
            "SOLICITADA",
            "https://example.com/job-b",
            "Indeed",
            "job-b"
    );

    repository.save(firstOffer);
    repository.save(secondOffer);

    List<JobOfferEntity> result = repository.findAll(
            "Empresa A",
            null,
            null,
            null
    );

    assertEquals(1, result.size());
    assertEquals("Empresa A", result.get(0).getCompany());
}
    @Test
    void shouldFilterByLocation() {
    JobOfferEntity firstOffer = createJobOffer(
            "Empresa A",
            "Backend Developer",
            "Madrid",
            "REMOTO",
            "PENDIENTE",
            "https://example.com/job-a",
            "LinkedIn",
            "job-a"
    );

    JobOfferEntity secondOffer = createJobOffer(
            "Empresa B",
            "Java Developer",
            "Barcelona",
            "HIBRIDO",
            "SOLICITADA",
            "https://example.com/job-b",
            "Indeed",
            "job-b"
    );

    repository.save(firstOffer);
    repository.save(secondOffer);

    List<JobOfferEntity> result = repository.findAll(
            null,
            "Madrid",
            null,
            null
    );

    assertEquals(1, result.size());
    assertEquals("Madrid", result.get(0).getLocation());
}
    @Test
    void shouldFilterByWorkMode() {
    JobOfferEntity firstOffer = createJobOffer(
            "Empresa A",
            "Backend Developer",
            "Madrid",
            "REMOTO",
            "PENDIENTE",
            "https://example.com/job-a",
            "LinkedIn",
            "job-a"
    );

    JobOfferEntity secondOffer = createJobOffer(
            "Empresa B",
            "Java Developer",
            "Barcelona",
            "HIBRIDO",
            "SOLICITADA",
            "https://example.com/job-b",
            "Indeed",
            "job-b"
    );

    repository.save(firstOffer);
    repository.save(secondOffer);

    List<JobOfferEntity> result = repository.findAll(
            null,
            null,
            "REMOTO",
            null
    );

    assertEquals(1, result.size());
    assertEquals("REMOTO", result.get(0).getWorkMode());
}
    @Test
    void shouldFilterByStatus() {
    JobOfferEntity firstOffer = createJobOffer(
            "Empresa A",
            "Backend Developer",
            "Madrid",
            "REMOTO",
            "PENDIENTE",
            "https://example.com/job-a",
            "LinkedIn",
            "job-a"
    );

    JobOfferEntity secondOffer = createJobOffer(
            "Empresa B",
            "Java Developer",
            "Barcelona",
            "HIBRIDO",
            "SOLICITADA",
            "https://example.com/job-b",
            "Indeed",
            "job-b"
    );

    repository.save(firstOffer);
    repository.save(secondOffer);

    List<JobOfferEntity> result = repository.findAll(
            null,
            null,
            null,
            "SOLICITADA"
    );

    assertEquals(1, result.size());
    assertEquals("SOLICITADA", result.get(0).getStatus());
}
    @Test
    void shouldFilterByLocationWorkModeAndStatus() {
    JobOfferEntity firstOffer = createJobOffer(
            "Empresa A",
            "Backend Developer",
            "Madrid",
            "REMOTO",
            "SOLICITADA",
            "https://example.com/job-a",
            "LinkedIn",
            "job-a"
    );

    JobOfferEntity secondOffer = createJobOffer(
            "Empresa B",
            "Java Developer",
            "Madrid",
            "REMOTO",
            "PENDIENTE",
            "https://example.com/job-b",
            "Indeed",
            "job-b"
    );

    JobOfferEntity thirdOffer = createJobOffer(
            "Empresa C",
            "Java Developer",
            "Barcelona",
            "REMOTO",
            "SOLICITADA",
            "https://example.com/job-c",
            "InfoJobs",
            "job-c"
    );

    repository.save(firstOffer);
    repository.save(secondOffer);
    repository.save(thirdOffer);

    List<JobOfferEntity> result = repository.findAll(
            null,
            "Madrid",
            "REMOTO",
            "SOLICITADA"
    );

    assertEquals(1, result.size());
    assertEquals("Empresa A", result.get(0).getCompany());
}
    @Test
    void shouldFilterByCompanyLocationWorkModeAndStatus() {
    JobOfferEntity firstOffer = createJobOffer(
            "Empresa A",
            "Backend Developer",
            "Madrid",
            "REMOTO",
            "SOLICITADA",
            "https://example.com/job-a",
            "LinkedIn",
            "job-a"
    );

    JobOfferEntity secondOffer = createJobOffer(
            "Empresa B",
            "Backend Developer",
            "Madrid",
            "REMOTO",
            "SOLICITADA",
            "https://example.com/job-b",
            "Indeed",
            "job-b"
    );

    JobOfferEntity thirdOffer = createJobOffer(
            "Empresa A",
            "Java Developer",
            "Barcelona",
            "REMOTO",
            "SOLICITADA",
            "https://example.com/job-c",
            "InfoJobs",
            "job-c"
    );

    repository.save(firstOffer);
    repository.save(secondOffer);
    repository.save(thirdOffer);

    List<JobOfferEntity> result = repository.findAll(
            "Empresa A",
            "Madrid",
            "REMOTO",
            "SOLICITADA"
    );

    assertEquals(1, result.size());
    assertEquals("Empresa A", result.get(0).getCompany());
    assertEquals("Madrid", result.get(0).getLocation());
    assertEquals("REMOTO", result.get(0).getWorkMode());
    assertEquals("SOLICITADA", result.get(0).getStatus());
}
    @Test
    void shouldReturnEmptyListWhenNoJobOfferMatchesFilters() {
    JobOfferEntity offer = createJobOffer(
            "Empresa A",
            "Backend Developer",
            "Madrid",
            "REMOTO",
            "PENDIENTE",
            "https://example.com/job-a",
            "LinkedIn",
            "job-a"
    );

    repository.save(offer);

    List<JobOfferEntity> result = repository.findAll(
            null,
            "Barcelona",
            null,
            null
    );

    assertEquals(0, result.size());
}
    @Test
    void shouldMatchLocationExactly() {
    JobOfferEntity madridOffer = createJobOffer(
            "Empresa A",
            "Backend Developer",
            "Madrid",
            "REMOTO",
            "PENDIENTE",
            "https://example.com/job-a",
            "LinkedIn",
            "job-a"
    );

    JobOfferEntity madridCentroOffer = createJobOffer(
            "Empresa B",
            "Java Developer",
            "Madrid Centro",
            "REMOTO",
            "PENDIENTE",
            "https://example.com/job-b",
            "Indeed",
            "job-b"
    );

    repository.save(madridOffer);
    repository.save(madridCentroOffer);

    List<JobOfferEntity> result = repository.findAll(
            null,
            "Madrid",
            null,
            null
    );

    assertEquals(1, result.size());
    assertEquals("Madrid", result.get(0).getLocation());
}
}