package com.opc.jobradar.application.service;

import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.springframework.stereotype.Service;

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
 * para garantizar que contiene una URL válida y que no existe otra
 * oferta registrada con la misma URL.
 *
 * @param jobOffer oferta de empleo que se quiere crear
 * @return oferta de empleo guardada
 * @throws IllegalArgumentException si la oferta no tiene una URL válida
 *                                  o si la URL ya está registrada
 */
public JobOffer create(JobOffer jobOffer) {

    // Comprobamos que la oferta tenga una URL válida antes
    // de realizar cualquier operación contra el repositorio.
    if (jobOffer.getUrl() == null || jobOffer.getUrl().isBlank()) {
        throw new IllegalArgumentException(
                "Job offer URL cannot be null or blank."
        );
    }

    // Comprobamos si ya existe una oferta con la misma URL.
    if (jobOfferRepository.existsByUrl(jobOffer.getUrl())) {
        throw new IllegalArgumentException(
                "Job offer with the same URL already exists."
        );
    }

    // La oferta ha superado las validaciones y puede persistirse.
    return jobOfferRepository.save(jobOffer);
}
}
