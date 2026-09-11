package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferNotFoundException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.domain.model.JobOfferStatusHistory;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import com.opc.jobradar.domain.port.out.JobOfferStatusHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

/**
 * Caso de uso que actualiza el estado de una oferta de empleo existente.
 *
 * Recibe el estado como texto para representar una entrada externa y lo
 * convierte al tipo de dominio antes de modificar la oferta. El estado
 * anterior no limita el nuevo estado permitido.
 */
@Service
public class UpdateJobOfferStatusService {

    private final JobOfferRepository jobOfferRepository;
    private final JobOfferStatusHistoryRepository jobOfferStatusHistoryRepository;

    public UpdateJobOfferStatusService(
            JobOfferRepository jobOfferRepository,
            JobOfferStatusHistoryRepository jobOfferStatusHistoryRepository
    ) {
        this.jobOfferRepository = jobOfferRepository;
        this.jobOfferStatusHistoryRepository =
                jobOfferStatusHistoryRepository;
    }

    /**
     * Actualiza el estado de una oferta identificada por su ID y registra
     * el nuevo estado en el historial.
     *
     * @param id identificador interno de la oferta
     * @param status estado recibido desde la entrada externa
     * @return oferta con el estado actualizado y persistido
     * @throws IllegalArgumentException si el ID es {@code null} o el estado
     *                                  no pertenece a {@link JobOfferStatus}
     * @throws JobOfferNotFoundException si no existe una oferta con ese ID
     */
    @Transactional
    public JobOffer updateStatus(Long id, String status) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "Job offer ID cannot be null."
            );
        }

        JobOfferStatus newStatus = toJobOfferStatus(status);

        JobOffer jobOffer = jobOfferRepository.findById(id)
                .orElseThrow(JobOfferNotFoundException::new);

        jobOffer.setStatus(newStatus);

        JobOffer savedJobOffer = jobOfferRepository.save(jobOffer);

        JobOfferStatusHistory history = new JobOfferStatusHistory();
        history.setJobOfferId(savedJobOffer.getId());
        history.setStatus(savedJobOffer.getStatus());
        history.setChangedAt(OffsetDateTime.now());

        jobOfferStatusHistoryRepository.save(history);

        return savedJobOffer;
    }

    private JobOfferStatus toJobOfferStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException(
                    "Job offer status cannot be null."
            );
        }

        try {
            return JobOfferStatus.valueOf(status);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Job offer status is not valid.",
                    exception
            );
        }
    }
}