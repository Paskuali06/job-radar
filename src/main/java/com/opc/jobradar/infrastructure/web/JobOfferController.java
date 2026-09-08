package com.opc.jobradar.infrastructure.web;

import com.opc.jobradar.application.service.CreateJobOfferService;
import com.opc.jobradar.domain.model.JobOffer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Adaptador HTTP para la gestión de ofertas de empleo.
 */
@RestController
@RequestMapping("/job-offers")
public class JobOfferController {

    private final CreateJobOfferService createJobOfferService;

    public JobOfferController(CreateJobOfferService createJobOfferService) {
        this.createJobOfferService = createJobOfferService;
    }

    /**
     * Crea una nueva oferta de empleo.
     *
     * @param request datos recibidos mediante HTTP
     * @return oferta creada con HTTP 201 Created
     */
    @PostMapping
    public ResponseEntity<JobOffer> create(@RequestBody CreateJobOfferRequest request) {
        JobOffer jobOffer = new JobOffer(
                null,
                request.company(),
                request.title(),
                request.location(),
                request.workMode(),
                request.url(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                request.source(),
                request.externalId()
        );

        JobOffer createdJobOffer = createJobOfferService.create(jobOffer);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdJobOffer.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdJobOffer);
    }
}