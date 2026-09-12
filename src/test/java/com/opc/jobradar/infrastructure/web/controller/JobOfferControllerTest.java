package com.opc.jobradar.infrastructure.web.controller;

import com.opc.jobradar.application.exception.JobOfferNotFoundException;
import com.opc.jobradar.application.service.DeleteJobOfferService;
import com.opc.jobradar.application.service.CreateJobOfferService;
import com.opc.jobradar.application.service.GetJobOfferService;
import com.opc.jobradar.application.service.GetJobOffersService;
import com.opc.jobradar.application.service.UpdateJobOfferStatusService;
import com.opc.jobradar.domain.model.FiltersByJobOffer;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.infrastructure.web.controller.CreateJobOfferRequest;
import com.opc.jobradar.infrastructure.web.controller.JobOfferController;
import com.opc.jobradar.application.service.UpdateJobOfferService;
import com.opc.jobradar.application.exception.JobOfferAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@WebMvcTest(JobOfferController.class)
class JobOfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateJobOfferService createJobOfferService;

    @MockitoBean
    private GetJobOfferService getJobOfferService;

    @MockitoBean
    private GetJobOffersService getJobOffersService;

    @MockitoBean
    private UpdateJobOfferStatusService updateJobOfferStatusService;
    
    @MockitoBean
    private DeleteJobOfferService deleteJobOfferService;

    @MockitoBean
    private UpdateJobOfferService updateJobOfferService;

    @Test
    void shouldCreateJobOffer() throws Exception {
        JobOffer createdJobOffer = new JobOffer(
                1L,
                "Empresa A",
                "Backend Developer",
                "Madrid",
                "REMOTO",
                "https://example.com/job-a",
                null,
                JobOfferStatus.PENDIENTE,
                null,
                null,
                null,
                null,
                null,
                "LinkedIn",
                "job-a"
        );

        when(createJobOfferService.create(any(JobOffer.class)))
                .thenReturn(createdJobOffer);

        String requestBody = """
                {
                    "company": "Empresa A",
                    "title": "Backend Developer",
                    "location": "Madrid",
                    "workMode": "REMOTO",
                    "url": "https://example.com/job-a",
                    "source": "LinkedIn",
                    "externalId": "job-a"
                }
                """;

        mockMvc.perform(post("/job-offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "id": 1,
                            "company": "Empresa A",
                            "title": "Backend Developer",
                            "location": "Madrid",
                            "workMode": "REMOTO",
                            "url": "https://example.com/job-a",
                            "status": "PENDIENTE",
                            "source": "LinkedIn",
                            "externalId": "job-a"
                        }
                        """));
    }

    @Test
    void shouldGetJobOfferById() throws Exception {
        JobOffer jobOffer = new JobOffer(
                1L,
                "Empresa A",
                "Backend Developer",
                "Madrid",
                "REMOTO",
                "https://example.com/job-a",
                null,
                JobOfferStatus.PENDIENTE,
                null,
                null,
                null,
                null,
                null,
                "LinkedIn",
                "job-a"
        );

        when(getJobOfferService.getById(1L))
                .thenReturn(jobOffer);

        mockMvc.perform(get("/job-offers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "id": 1,
                            "company": "Empresa A",
                            "title": "Backend Developer",
                            "location": "Madrid",
                            "workMode": "REMOTO",
                            "url": "https://example.com/job-a",
                            "status": "PENDIENTE",
                            "source": "LinkedIn",
                            "externalId": "job-a"
                        }
                        """));
    }

    @Test
    void shouldReturnNotFoundWhenJobOfferDoesNotExist() throws Exception {
        when(getJobOfferService.getById(1L))
                .thenThrow(new JobOfferNotFoundException());

        mockMvc.perform(get("/job-offers/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllJobOffersWithoutFilters() throws Exception {
        JobOffer jobOffer = new JobOffer(
                1L,
                "Empresa A",
                "Backend Developer",
                "Madrid",
                "REMOTO",
                "https://example.com/job-a",
                null,
                JobOfferStatus.PENDIENTE,
                null,
                null,
                null,
                null,
                null,
                "LinkedIn",
                "job-a"
        );

        when(getJobOffersService.get(any(FiltersByJobOffer.class)))
                .thenReturn(List.of(jobOffer));

        mockMvc.perform(get("/job-offers"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        [
                            {
                                "id": 1,
                                "company": "Empresa A",
                                "title": "Backend Developer",
                                "location": "Madrid",
                                "workMode": "REMOTO",
                                "url": "https://example.com/job-a",
                                "status": "PENDIENTE",
                                "source": "LinkedIn",
                                "externalId": "job-a"
                            }
                        ]
                        """));
    }

    @Test
    void shouldFilterJobOffersByLocation() throws Exception {
        JobOffer jobOffer = new JobOffer(
                1L,
                "Empresa A",
                "Backend Developer",
                "Madrid",
                "REMOTO",
                "https://example.com/job-a",
                null,
                JobOfferStatus.PENDIENTE,
                null,
                null,
                null,
                null,
                null,
                "LinkedIn",
                "job-a"
        );

        when(getJobOffersService.get(any(FiltersByJobOffer.class)))
                .thenReturn(List.of(jobOffer));

        mockMvc.perform(get("/job-offers")
                        .param("location", "Madrid"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        [
                            {
                                "id": 1,
                                "company": "Empresa A",
                                "title": "Backend Developer",
                                "location": "Madrid",
                                "workMode": "REMOTO",
                                "url": "https://example.com/job-a",
                                "status": "PENDIENTE",
                                "source": "LinkedIn",
                                "externalId": "job-a"
                            }
                        ]
                        """));
    }

    @Test
    void shouldFilterJobOffersByLocationAndStatus() throws Exception {
        JobOffer jobOffer = new JobOffer(
                1L,
                "Empresa A",
                "Backend Developer",
                "Madrid",
                "REMOTO",
                "https://example.com/job-a",
                null,
                JobOfferStatus.SOLICITADA,
                null,
                null,
                null,
                null,
                null,
                "LinkedIn",
                "job-a"
        );

        when(getJobOffersService.get(any(FiltersByJobOffer.class)))
                .thenReturn(List.of(jobOffer));

        mockMvc.perform(get("/job-offers")
                        .param("location", "Madrid")
                        .param("status", "SOLICITADA"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        [
                            {
                                "id": 1,
                                "company": "Empresa A",
                                "title": "Backend Developer",
                                "location": "Madrid",
                                "workMode": "REMOTO",
                                "url": "https://example.com/job-a",
                                "status": "SOLICITADA",
                                "source": "LinkedIn",
                                "externalId": "job-a"
                            }
                        ]
                        """));
    }

    @Test
    void shouldReturnEmptyListWhenNoJobOffersMatch() throws Exception {
        when(getJobOffersService.get(any(FiltersByJobOffer.class)))
                .thenReturn(List.of());

        mockMvc.perform(get("/job-offers")
                        .param("location", "Barcelona"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldUpdateJobOfferStatus() throws Exception {
        JobOffer updatedJobOffer = new JobOffer(
                1L,
                "Empresa A",
                "Backend Developer",
                "Madrid",
                "REMOTO",
                "https://example.com/job-a",
                null,
                JobOfferStatus.SOLICITADA,
                null,
                null,
                null,
                null,
                null,
                "LinkedIn",
                "job-a"
        );

        when(updateJobOfferStatusService.updateStatus(1L, "SOLICITADA"))
                .thenReturn(updatedJobOffer);

        mockMvc.perform(patch("/job-offers/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "status": "SOLICITADA"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "id": 1,
                            "company": "Empresa A",
                            "title": "Backend Developer",
                            "location": "Madrid",
                            "workMode": "REMOTO",
                            "url": "https://example.com/job-a",
                            "status": "SOLICITADA",
                            "source": "LinkedIn",
                            "externalId": "job-a"
                        }
                        """));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingStatusOfNonExistingJobOffer() throws Exception {
        when(updateJobOfferStatusService.updateStatus(1L, "SOLICITADA"))
                .thenThrow(new JobOfferNotFoundException());

        mockMvc.perform(patch("/job-offers/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "status": "SOLICITADA"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenStatusIsInvalid() throws Exception {
        when(updateJobOfferStatusService.updateStatus(1L, "INVALIDO"))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(patch("/job-offers/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "status": "INVALIDO"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldUpdateJobOffer() throws Exception {
    JobOffer updatedJobOffer = new JobOffer(
            1L,
            "Nueva Empresa",
            "Nuevo título",
            "Barcelona",
            "REMOTO",
            "https://example.com/new-job",
            null,
            JobOfferStatus.PENDIENTE,
            null,
            null,
            null,
            null,
            null,
            "LinkedIn",
            "job-a"
    );

    when(updateJobOfferService.update(
            org.mockito.ArgumentMatchers.eq(1L),
            any(JobOffer.class)
    )).thenReturn(updatedJobOffer);

    mockMvc.perform(put("/job-offers/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "company": "Nueva Empresa",
                                "title": "Nuevo título",
                                "location": "Barcelona",
                                "workMode": "REMOTO",
                                "url": "https://example.com/new-job",
                                "source": "LinkedIn",
                                "externalId": "job-a"
                            }
                            """))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json("""
                    {
                        "id": 1,
                        "company": "Nueva Empresa",
                        "title": "Nuevo título",
                        "location": "Barcelona",
                        "workMode": "REMOTO",
                        "url": "https://example.com/new-job",
                        "status": "PENDIENTE",
                        "source": "LinkedIn",
                        "externalId": "job-a"
                    }
                    """));
}

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingJobOffer() throws Exception {
    when(updateJobOfferService.update(
            org.mockito.ArgumentMatchers.eq(1L),
            any(JobOffer.class)
    )).thenThrow(new JobOfferNotFoundException());

    mockMvc.perform(put("/job-offers/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "company": "Nueva Empresa",
                                "title": "Nuevo título",
                                "location": "Barcelona",
                                "workMode": "REMOTO",
                                "url": "https://example.com/new-job",
                                "source": "LinkedIn",
                                "externalId": "job-a"
                            }
                            """))
            .andExpect(status().isNotFound());
}
    @Test
    void shouldReturnConflictWhenCreatingExistingJobOffer() throws Exception {
    CreateJobOfferRequest request = new CreateJobOfferRequest(
            "Company",
            "Java Developer",
            "Madrid",
            "Remoto",
            "https://example.com/job",
            "LINKEDIN",
            "123"
    );

    when(createJobOfferService.create(any(JobOffer.class)))
            .thenThrow(new JobOfferAlreadyExistsException());

    mockMvc.perform(post("/job-offers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "company": "Company",
                                "title": "Java Developer",
                                "location": "Madrid",
                                "workMode": "Remoto",
                                "url": "https://example.com/job",
                                "source": "LINKEDIN",
                                "externalId": "123"
                            }
                            """))
            .andExpect(status().isConflict())
            .andExpect(content().string("Esta oferta ya existe"));
}
    @Test
    void shouldReturnBadRequestWhenCompanyIsBlank() throws Exception {
        mockMvc.perform(post("/job-offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "company": "",
                                    "title": "Java Developer",
                                    "location": "Madrid",
                                    "workMode": "Remoto",
                                    "url": "https://example.com/job",
                                    "source": "LINKEDIN",
                                    "externalId": "123"
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(createJobOfferService, never()).create(any(JobOffer.class));
    }

    @Test
    void shouldReturnBadRequestWhenTitleIsBlank() throws Exception {
        mockMvc.perform(post("/job-offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "company": "Company",
                                    "title": "",
                                    "location": "Madrid",
                                    "workMode": "Remoto",
                                    "url": "https://example.com/job",
                                    "source": "LINKEDIN",
                                    "externalId": "123"
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(createJobOfferService, never()).create(any(JobOffer.class));
    }

    @Test
    void shouldReturnBadRequestWhenUrlIsBlank() throws Exception {
        mockMvc.perform(post("/job-offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "company": "Company",
                                    "title": "Java Developer",
                                    "location": "Madrid",
                                    "workMode": "Remoto",
                                    "url": "",
                                    "source": "LINKEDIN",
                                    "externalId": "123"
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(createJobOfferService, never()).create(any(JobOffer.class));
    }

    @Test
    void shouldReturnBadRequestWhenSourceIsBlank() throws Exception {
        mockMvc.perform(post("/job-offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "company": "Company",
                                    "title": "Java Developer",
                                    "location": "Madrid",
                                    "workMode": "Remoto",
                                    "url": "https://example.com/job",
                                    "source": "",
                                    "externalId": "123"
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(createJobOfferService, never()).create(any(JobOffer.class));
    }

    @Test
    void shouldReturnBadRequestWhenExternalIdIsBlank() throws Exception {
        mockMvc.perform(post("/job-offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "company": "Company",
                                    "title": "Java Developer",
                                    "location": "Madrid",
                                    "workMode": "Remoto",
                                    "url": "https://example.com/job",
                                    "source": "LINKEDIN",
                                    "externalId": ""
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(createJobOfferService, never()).create(any(JobOffer.class));
    }
        @Test
    void shouldReturnBadRequestWhenUpdatingJobOfferWithBlankCompany() throws Exception {
        mockMvc.perform(put("/job-offers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "company": "",
                                    "title": "Nuevo título",
                                    "location": "Barcelona",
                                    "workMode": "REMOTO",
                                    "url": "https://example.com/new-job"
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(updateJobOfferService, never())
                .update(any(Long.class), any(JobOffer.class));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingJobOfferWithBlankTitle() throws Exception {
        mockMvc.perform(put("/job-offers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "company": "Nueva Empresa",
                                    "title": "",
                                    "location": "Barcelona",
                                    "workMode": "REMOTO",
                                    "url": "https://example.com/new-job"
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(updateJobOfferService, never())
                .update(any(Long.class), any(JobOffer.class));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingJobOfferWithBlankUrl() throws Exception {
        mockMvc.perform(put("/job-offers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "company": "Nueva Empresa",
                                    "title": "Nuevo título",
                                    "location": "Barcelona",
                                    "workMode": "REMOTO",
                                    "url": ""
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(updateJobOfferService, never())
                .update(any(Long.class), any(JobOffer.class));
    }
}