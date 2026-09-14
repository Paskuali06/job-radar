package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferNotFoundException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de uso que actualiza los datos de una oferta de empleo existente.
 */
@Service
public class UpdateJobOfferService {

    private final JobOfferRepository jobOfferRepository;

    public UpdateJobOfferService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    /**
     * Actualiza los datos permitidos de una oferta existente perteneciente al usuario.
     *
     * @param id identificador interno de la oferta
     * @param userId identificador del usuario propietario
     * @param updatedJobOffer datos nuevos de la oferta
     * @return oferta actualizada y persistida
     * @throws IllegalArgumentException si el identificador o el usuario son {@code null}
     * @throws JobOfferNotFoundException si la oferta no existe para el usuario
     */
    public JobOffer update(Long id, Long userId, JobOffer updatedJobOffer) {
        if (id == null) {
            throw new IllegalArgumentException("Job offer ID cannot be null.");
        }

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }

        JobOffer existingJobOffer = jobOfferRepository.findByIdAndUserId(id, userId)
                .orElseThrow(JobOfferNotFoundException::new);

        existingJobOffer.setCompany(updatedJobOffer.getCompany());
        existingJobOffer.setTitle(updatedJobOffer.getTitle());
        existingJobOffer.setLocation(updatedJobOffer.getLocation());
        existingJobOffer.setWorkMode(updatedJobOffer.getWorkMode());
        existingJobOffer.setUrl(updatedJobOffer.getUrl());
        existingJobOffer.setPublishedAt(updatedJobOffer.getPublishedAt());
        existingJobOffer.setScore(updatedJobOffer.getScore());
        existingJobOffer.setClassification(updatedJobOffer.getClassification());
        existingJobOffer.setDescription(updatedJobOffer.getDescription());

        return jobOfferRepository.save(existingJobOffer);
    }
}