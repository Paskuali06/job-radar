package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.JobOfferAlreadyExistsException;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.model.JobOfferStatus;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona la ingesta de ofertas de empleo procedentes de fuentes externas.
 */
@Service
public class IngestJobOfferService {

    private final JobOfferRepository repository;

    public IngestJobOfferService(JobOfferRepository repository) {
        this.repository = repository;
    }

    /**
     * Ingresa una oferta externa asociándola al usuario indicado.
     *
     * @param userId identificador del usuario propietario
     * @param externalJobOffer oferta recibida desde una fuente externa
     * @return oferta persistida
     * @throws JobOfferAlreadyExistsException si la oferta ya existe para
     *         ese usuario, fuente e identificador externo
     */
    public JobOffer ingest(
            Long userId,
            ExternalJobOffer externalJobOffer
    ) {
        validate(externalJobOffer);

        ExternalJobOffer normalizedOffer = normalize(externalJobOffer);

        boolean alreadyExists =
                repository.existsByUserIdAndSourceAndExternalId(
                        userId,
                        normalizedOffer.source(),
                        normalizedOffer.externalId()
                );

        if (alreadyExists) {
            throw new JobOfferAlreadyExistsException();
        }

        JobOffer jobOffer = new JobOffer(
                null,
                normalizedOffer.company(),
                normalizedOffer.title(),
                normalizedOffer.location(),
                normalizedOffer.workMode(),
                normalizedOffer.url(),
                normalizedOffer.publishedAt(),
                JobOfferStatus.PENDIENTE,
                normalizedOffer.description(),
                null,
                null,
                normalizedOffer.source(),
                normalizedOffer.externalId()
        );

        jobOffer.setUserId(userId);

        return repository.save(jobOffer);
    }

    /**
     * Ingresa varias ofertas externas para un usuario.
     *
     * Las ofertas inválidas o duplicadas se descartan individualmente
     * para permitir que el resto del lote continúe procesándose.
     *
     * @param userId identificador del usuario propietario
     * @param externalJobOffers ofertas recibidas desde fuentes externas
     * @return ofertas que han sido ingeridas correctamente
     */
    public List<JobOffer> ingestAll(
            Long userId,
            List<ExternalJobOffer> externalJobOffers
    ) {
        List<JobOffer> ingestedOffers = new ArrayList<>();

        for (ExternalJobOffer externalJobOffer : externalJobOffers) {
            try {
                ingestedOffers.add(ingest(userId, externalJobOffer));
            } catch (
                    IllegalArgumentException
                    | JobOfferAlreadyExistsException exception
            ) {
                // Una oferta rechazada no debe impedir procesar las siguientes.
            }
        }

        return ingestedOffers;
    }

    /**
     * Valida los datos obligatorios de una oferta externa.
     *
     * @param externalJobOffer oferta que se quiere validar
     * @throws IllegalArgumentException si falta algún dato obligatorio
     */
    private void validate(ExternalJobOffer externalJobOffer) {
        if (externalJobOffer == null) {
            throw new IllegalArgumentException(
                    "La oferta externa es obligatoria"
            );
        }

        if (isBlank(externalJobOffer.source())) {
            throw new IllegalArgumentException(
                    "source es obligatorio"
            );
        }

        if (isBlank(externalJobOffer.externalId())) {
            throw new IllegalArgumentException(
                    "externalId es obligatorio"
            );
        }

        if (isBlank(externalJobOffer.company())) {
            throw new IllegalArgumentException(
                    "company es obligatorio"
            );
        }

        if (isBlank(externalJobOffer.title())) {
            throw new IllegalArgumentException(
                    "title es obligatorio"
            );
        }

        if (isBlank(externalJobOffer.url())) {
            throw new IllegalArgumentException(
                    "url es obligatorio"
            );
        }

        if (externalJobOffer.publishedAt() == null) {
            throw new IllegalArgumentException(
                    "publishedAt es obligatorio"
            );
        }
    }

    /**
     * Normaliza los datos de una oferta externa antes de persistirla.
     *
     * @param externalJobOffer oferta que se quiere normalizar
     * @return oferta con sus datos normalizados
     */
    private ExternalJobOffer normalize(
            ExternalJobOffer externalJobOffer
    ) {
        return new ExternalJobOffer(
                externalJobOffer.source().trim().toUpperCase(),
                externalJobOffer.externalId().trim(),
                externalJobOffer.company().trim(),
                externalJobOffer.title().trim(),
                trimNullable(externalJobOffer.location()),
                normalizeWorkMode(externalJobOffer.workMode()),
                externalJobOffer.url().trim(),
                trimNullable(externalJobOffer.description()),
                externalJobOffer.publishedAt()
        );
    }

    /**
     * Normaliza un valor opcional conservando {@code null}.
     *
     * @param value valor que se quiere normalizar
     * @return valor sin espacios laterales o {@code null}
     */
    private String trimNullable(String value) {
        return value == null ? null : value.trim();
    }

    /**
     * Normaliza el modo de trabajo conservando {@code null}.
     *
     * @param workMode modo de trabajo
     * @return modo de trabajo normalizado o {@code null}
     */
    private String normalizeWorkMode(String workMode) {
        return workMode == null
                ? null
                : workMode.trim().toUpperCase();
    }

    /**
     * Comprueba si una cadena es nula, vacía o contiene únicamente espacios.
     *
     * @param value valor que se quiere comprobar
     * @return {@code true} si el valor está vacío
     */
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}