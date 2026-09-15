package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferAlreadyExistsException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.domain.model.JobOfferStatusHistory;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import com.opc.jobradar.domain.port.out.JobOfferStatusHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;

/**
 * Caso de uso que crea ofertas de empleo después de validar los datos mínimos
 * y su identidad dentro de una fuente.
 */
@Service
public class CreateJobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final JobOfferStatusHistoryRepository jobOfferStatusHistoryRepository;

    public CreateJobOfferService(
            JobOfferRepository jobOfferRepository,
            JobOfferStatusHistoryRepository jobOfferStatusHistoryRepository
    ) {
        this.jobOfferRepository = jobOfferRepository;
        this.jobOfferStatusHistoryRepository =
                jobOfferStatusHistoryRepository;
    }

    /**
     * Crea una oferta de empleo y registra su estado inicial en el historial.
     *
     * @param jobOffer oferta que se desea crear
     * @return oferta creada y persistida
     * @throws IllegalArgumentException si la oferta o alguno de sus datos
     *                                  obligatorios no es válido
     * @throws JobOfferAlreadyExistsException si ya existe una oferta con la
     *                                        misma fuente e identificador externo
     *                                        para el mismo usuario
     */
    @Transactional
    public JobOffer create(JobOffer jobOffer) {

        if (jobOffer == null) {
            throw new IllegalArgumentException(
                    "Job offer cannot be null."
            );
        }

        if (jobOffer.getUserId() == null) {
            throw new IllegalArgumentException(
                    "User ID cannot be null."
            );
        }

        if (jobOffer.getUrl() == null || jobOffer.getUrl().isBlank()) {
            throw new IllegalArgumentException(
                    "Job offer URL cannot be null or blank."
            );
        }

        if (!isValidUrl(jobOffer.getUrl())) {
            throw new IllegalArgumentException(
                    "Job offer URL is not valid."
            );
        }

        if (jobOffer.getSource() == null || jobOffer.getSource().isBlank()) {
            throw new IllegalArgumentException(
                    "Job offer source cannot be null or blank."
            );
        }

        if (jobOffer.getExternalId() == null
                || jobOffer.getExternalId().isBlank()) {
            throw new IllegalArgumentException(
                    "Job offer external ID cannot be null or blank."
            );
        }

        boolean alreadyExists =
                jobOfferRepository.existsByUserIdAndSourceAndExternalId(
                        jobOffer.getUserId(),
                        jobOffer.getSource(),
                        jobOffer.getExternalId()
                );

        if (alreadyExists) {
            throw new JobOfferAlreadyExistsException();
        }

        if (jobOffer.getStatus() == null) {
            jobOffer.setStatus(JobOfferStatus.PENDIENTE);
        }

        OffsetDateTime now = OffsetDateTime.now();
        jobOffer.setCreatedAt(now);
        jobOffer.setUpdatedAt(now);

        JobOffer savedJobOffer = jobOfferRepository.save(jobOffer);

        JobOfferStatusHistory history = new JobOfferStatusHistory();
        history.setJobOfferId(savedJobOffer.getId());
        history.setStatus(savedJobOffer.getStatus());
        history.setChangedAt(OffsetDateTime.now());

        jobOfferStatusHistoryRepository.save(history);

        return savedJobOffer;
    }

    private boolean isValidUrl(String url) {
        try {
            URI uri = new URI(url);

            return ("http".equalsIgnoreCase(uri.getScheme())
                    || "https".equalsIgnoreCase(uri.getScheme()))
                    && uri.getHost() != null
                    && !uri.getHost().isBlank();

        } catch (URISyntaxException exception) {
            return false;
        }
    }
}