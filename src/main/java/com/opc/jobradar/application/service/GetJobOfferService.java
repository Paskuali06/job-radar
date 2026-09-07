package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferNotFoundException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.springframework.stereotype.Service;
import com.opc.jobradar.domain.model.FiltersByJobOffer;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso que obtiene una oferta de empleo a partir de su identificador
 * interno.
 */
@Service
public class GetJobOfferService {

    private final JobOfferRepository jobOfferRepository;

    public GetJobOfferService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    /**
     * Obtiene una oferta por su identificador.
     *
     * @param id identificador interno de la oferta
     * @return la oferta encontrada
     * @throws IllegalArgumentException si el identificador es {@code null}
     * @throws JobOfferNotFoundException si no existe una oferta con ese
     *                                   identificador
     */
    public JobOffer getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Job offer ID cannot be null.");
        }

        return jobOfferRepository.findById(id)
                .orElseThrow(JobOfferNotFoundException::new);
    }

@Service
public class GetJobOffersService {

    private final JobOfferRepository jobOfferRepository;

    public GetJobOffersService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    /**
     * Obtiene una oferta por su identificador.
     *
     * @param id identificador interno de la oferta
     * @return la oferta encontrada
     * @throws IllegalArgumentException si el identificador es {@code null}
     * @throws JobOfferNotFoundException si no existe una oferta con ese identificador
     */
    public List<JobOffer> get(FiltersByJobOffer filters) {
        return jobOfferRepository.findAll(filters);
    }
}
}
