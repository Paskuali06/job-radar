package com.opc.jobradar.infrastructure.web;

import com.opc.jobradar.application.exception.JobOfferNotFoundException;
import com.opc.jobradar.application.service.GetJobOfferService;
import com.opc.jobradar.application.service.CreateJobOfferService;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobOfferController.class)
class JobOfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateJobOfferService createJobOfferService;

    @MockitoBean
    private GetJobOfferService getJobOfferService;

    // ============================================================
    // CT-010 - El sistema debe permitir crear una oferta de empleo mediante una petición HTTP POST
    // ============================================================
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
}