package com.opc.jobradar.infrastructure.web;

import com.opc.jobradar.application.exception.JobOfferNotFoundException;
import com.opc.jobradar.application.service.CreateJobOfferService;
import com.opc.jobradar.application.service.GetJobOfferService;
import com.opc.jobradar.domain.model.JobOffer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final GetJobOfferService getJobOfferService;

    public JobOfferController(
            CreateJobOfferService createJobOfferService,
            GetJobOfferService getJobOfferService) {
        this.createJobOfferService = createJobOfferService;
        this.getJobOfferService = getJobOfferService;
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

    /**
     * Obtiene una oferta de empleo por su identificador.
     *
     * @param id identificador interno de la oferta
     * @return oferta encontrada con HTTP 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobOffer> getById(@PathVariable Long id) {
        JobOffer jobOffer = getJobOfferService.getById(id);
        return ResponseEntity.ok(jobOffer);
    }

    @ExceptionHandler(JobOfferNotFoundException.class)
    public ResponseEntity<Void> handleJobOfferNotFound() {
    return ResponseEntity.notFound().build();
}
}