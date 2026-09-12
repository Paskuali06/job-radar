package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferNotFoundException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de uso que obtiene una oferta de empleo a partir de su identificador
 * interno y su propietario.
 */
@Service
public class GetJobOfferService {

    private final JobOfferRepository jobOfferRepository;

    public GetJobOfferService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    /**
     * Obtiene una oferta por su identificador y su propietario.
     *
     * @param id identificador interno de la oferta
     * @param userId identificador del usuario propietario
     * @return la oferta encontrada
     * @throws IllegalArgumentException si el identificador de la oferta o del
     *                                  usuario es {@code null}
     * @throws JobOfferNotFoundException si no existe una oferta con ese
     *                                   identificador que pertenezca al usuario
     */
    public JobOffer getById(Long id, Long userId) {
        if (id == null) {
            throw new IllegalArgumentException("Job offer ID cannot be null.");
        }

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }

        return jobOfferRepository.findByIdAndUserId(id, userId)
                .orElseThrow(JobOfferNotFoundException::new);
    }
}