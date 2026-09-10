package com.opc.jobradar.domain.port.out;

import com.opc.jobradar.domain.model.FiltersByJobOffer;
import com.opc.jobradar.domain.model.JobOffer;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para almacenar y consultar ofertas de empleo.
 *
 * Los casos de uso dependen de este contrato para no conocer la tecnología
 * concreta utilizada para persistir las ofertas.
 */
public interface JobOfferRepository {

    /**
     * Guarda una oferta de empleo.
     *
     * @param jobOffer oferta que se quiere persistir
     * @return oferta persistida
     */
    JobOffer save(JobOffer jobOffer);

    /**
     * Busca una oferta por su identificador interno.
     *
     * @param id identificador de la oferta
     * @return la oferta encontrada, o un resultado vacío si no existe
     */
    Optional<JobOffer> findById(Long id);

    /**
     * Busca una oferta por su URL.
     *
     * @param url URL de la oferta
     * @return la oferta encontrada, o un resultado vacío si no existe
     */
    Optional<JobOffer> findByUrl(String url);

    /**
     * Indica si existe una oferta con la URL proporcionada.
     *
     * @param url URL que se quiere comprobar
     * @return {@code true} si existe una oferta con esa URL
     */
    boolean existsByUrl(String url);

    /**
     * Indica si existe una oferta con la identidad formada por fuente e
     * identificador externo.
     *
     * @param source fuente de la oferta
     * @param externalId identificador de la oferta en la fuente
     * @return {@code true} si ya existe esa identidad
     */
    boolean existsBySourceAndExternalId(String source, String externalId);

    /**
     * Consulta las ofertas de empleo aplicando los filtros proporcionados.
     *
     * @param filters filtros opcionales de consulta
     * @return lista de ofertas que cumplen los filtros
     */
    List<JobOffer> findAll(FiltersByJobOffer filters);

    /**
     * Elimina una oferta de empleo por su identificador interno.
     *
     * @param id identificador de la oferta que se quiere eliminar
     */
    void deleteById(Long id);
}