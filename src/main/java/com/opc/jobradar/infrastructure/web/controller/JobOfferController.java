package com.opc.jobradar.infrastructure.web.controller;

import com.opc.jobradar.application.exception.JobOfferAlreadyExistsException;
import com.opc.jobradar.application.exception.JobOfferNotFoundException;
import com.opc.jobradar.application.service.CreateJobOfferService;
import com.opc.jobradar.application.service.DeleteJobOfferService;
import com.opc.jobradar.application.service.GetJobOfferService;
import com.opc.jobradar.application.service.GetJobOffersService;
import com.opc.jobradar.application.service.UpdateJobOfferService;
import com.opc.jobradar.application.service.UpdateJobOfferStatusService;
import com.opc.jobradar.domain.model.FiltersByJobOffer;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.infrastructure.web.UpdateJobOfferRequest;
import com.opc.jobradar.infrastructure.web.UpdateJobOfferStatusRequest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Adaptador HTTP para la gestión de ofertas de empleo.
 */
@RestController
@RequestMapping("/job-offers")
public class JobOfferController {

    private final CreateJobOfferService createJobOfferService;
    private final GetJobOfferService getJobOfferService;
    private final GetJobOffersService getJobOffersService;
    private final UpdateJobOfferStatusService updateJobOfferStatusService;
    private final DeleteJobOfferService deleteJobOfferService;
    private final UpdateJobOfferService updateJobOfferService;

    public JobOfferController(
            CreateJobOfferService createJobOfferService,
            GetJobOfferService getJobOfferService,
            GetJobOffersService getJobOffersService,
            UpdateJobOfferStatusService updateJobOfferStatusService,
            DeleteJobOfferService deleteJobOfferService,
            UpdateJobOfferService updateJobOfferService) {
        this.createJobOfferService = createJobOfferService;
        this.getJobOfferService = getJobOfferService;
        this.getJobOffersService = getJobOffersService;
        this.updateJobOfferStatusService = updateJobOfferStatusService;
        this.deleteJobOfferService = deleteJobOfferService;
        this.updateJobOfferService = updateJobOfferService;
    }

    /**
     * Crea una nueva oferta de empleo.
     *
     * @param request datos recibidos mediante HTTP
     * @return oferta creada con HTTP 201 Created
     */
    @PostMapping
    public ResponseEntity<JobOffer> create(
            @Valid @RequestBody CreateJobOfferRequest request) {

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

    /**
     * Obtiene las ofertas de empleo aplicando filtros opcionales.
     *
     * @param company empresa
     * @param location ubicación
     * @param workMode modalidad de trabajo
     * @param status estado de la oferta
     * @return ofertas que cumplen los filtros
     */
    @GetMapping
    public ResponseEntity<List<JobOffer>> getAll(
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String workMode,
            @RequestParam(required = false) JobOfferStatus status) {

        FiltersByJobOffer filters = new FiltersByJobOffer(
                company,
                location,
                workMode,
                status
        );

        List<JobOffer> jobOffers = getJobOffersService.get(filters);

        return ResponseEntity.ok(jobOffers);
    }

    /**
     * Actualiza el estado de una oferta de empleo.
     *
     * @param id identificador interno de la oferta
     * @param request datos recibidos mediante HTTP
     * @return oferta actualizada con HTTP 200 OK
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<JobOffer> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateJobOfferStatusRequest request) {

        JobOffer updatedJobOffer = updateJobOfferStatusService.updateStatus(
                id,
                request.status()
        );

        return ResponseEntity.ok(updatedJobOffer);
    }

    /**
     * Convierte una oferta no encontrada en HTTP 404.
     *
     * @return respuesta HTTP 404
     */
    @ExceptionHandler(JobOfferNotFoundException.class)
    public ResponseEntity<Void> handleJobOfferNotFound() {
        return ResponseEntity.notFound().build();
    }

    /**
     * Convierte un error de entrada en HTTP 400.
     *
     * @return respuesta HTTP 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException() {
        return ResponseEntity.badRequest().build();
    }

    /**
     * Elimina una oferta de empleo.
     *
     * @param id identificador interno de la oferta
     * @return respuesta HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteJobOfferService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza los datos de una oferta de empleo.
     *
     * @param id identificador interno de la oferta
     * @param request datos recibidos mediante HTTP
     * @return oferta actualizada con HTTP 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<JobOffer> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateJobOfferRequest request) {

        JobOffer jobOffer = new JobOffer(
                null,
                request.company(),
                request.title(),
                request.location(),
                request.workMode(),
                request.url(),
                request.publishedAt(),
                null,
                request.score(),
                request.classification(),
                request.description(),
                null,
                null,
                null,
                null
        );

        JobOffer updatedJobOffer = updateJobOfferService.update(id, jobOffer);

        return ResponseEntity.ok(updatedJobOffer);
    }

    /**
     * Convierte un intento de crear una oferta duplicada en HTTP 409.
     *
     * @param exception excepción de oferta ya existente
     * @return respuesta HTTP 409 Conflict con el mensaje de la excepción
     */
    @ExceptionHandler(JobOfferAlreadyExistsException.class)
    public ResponseEntity<String> handleJobOfferAlreadyExists(
            JobOfferAlreadyExistsException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }
}