package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferNotFoundException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de uso que elimina una oferta de empleo existente.
 */
@Service
public class DeleteJobOfferService {

    private final JobOfferRepository jobOfferRepository;

    public DeleteJobOfferService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    /**
     * Elimina una oferta perteneciente al usuario autenticado.
     *
     * @param id identificador interno de la oferta
     * @param userId identificador del usuario autenticado
     * @throws IllegalArgumentException si el identificador o usuario son {@code null}
     * @throws JobOfferNotFoundException si la oferta no existe o no pertenece al usuario
     */
    public void delete(Long id, Long userId) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "Job offer ID cannot be null."
            );
        }

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID cannot be null."
            );
        }

        JobOffer jobOffer = jobOfferRepository.findByIdAndUserId(id, userId)
                .orElseThrow(JobOfferNotFoundException::new);

        jobOfferRepository.deleteById(jobOffer.getId());
    }
}