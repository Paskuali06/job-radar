package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferAlreadyExistsException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de uso que crea ofertas de empleo después de validar los datos mínimos
 * y su identidad dentro de una fuente.
 */
@Service
public class CreateJobOfferService {

    private final JobOfferRepository jobOfferRepository;

    public CreateJobOfferService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    /**
     * Crea una nueva oferta de empleo.
     *
     * Antes de guardar la oferta se realizan las validaciones necesarias
     * para garantizar que contiene una URL, una fuente y un identificador
     * externo válidos, y que no existe otra oferta con la misma identidad.
     *
     * La identidad de una oferta está formada por la combinación
     * de source y externalId.
     *
     * @param jobOffer oferta de empleo que se quiere crear
     * @return oferta de empleo guardada
     * @throws IllegalArgumentException si algún dato obligatorio no es válido
     * @throws JobOfferAlreadyExistsException si ya existe una oferta con la
     *                                         misma identidad
     */
    public JobOffer create(JobOffer jobOffer) {

        if (jobOffer.getUrl() == null || jobOffer.getUrl().isBlank()) {
            throw new IllegalArgumentException(
                    "Job offer URL cannot be null or blank."
            );
        }

        if (jobOffer.getSource() == null || jobOffer.getSource().isBlank()) {
            throw new IllegalArgumentException(
                    "Job offer source cannot be null or blank."
            );
        }

        if (jobOffer.getExternalId() == null || jobOffer.getExternalId().isBlank()) {
            throw new IllegalArgumentException(
                    "Job offer external ID cannot be null or blank."
            );
        }

        if (jobOfferRepository.existsBySourceAndExternalId(
                jobOffer.getSource(),
                jobOffer.getExternalId()
        )) {
            throw new JobOfferAlreadyExistsException();
        }

        if (jobOffer.getStatus() == null) {
            jobOffer.setStatus(JobOfferStatus.PENDIENTE);
        }

        return jobOfferRepository.save(jobOffer);
    }
}