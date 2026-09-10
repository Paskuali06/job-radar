package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferNotFoundException;
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
     * Elimina una oferta por su identificador.
     *
     * @param id identificador interno de la oferta
     * @throws IllegalArgumentException si el identificador es {@code null}
     * @throws JobOfferNotFoundException si no existe la oferta
     */
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Job offer ID cannot be null.");
        }

        jobOfferRepository.findById(id)
                .orElseThrow(JobOfferNotFoundException::new);

        jobOfferRepository.deleteById(id);
    }
}