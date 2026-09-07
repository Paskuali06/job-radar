package com.opc.jobradar.infrastructure.persistence.repository;

import com.opc.jobradar.infrastructure.persistence.entity.JobOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio técnico de Spring Data JPA para la entidad de ofertas de
 * empleo.
 *
 * El adaptador de persistencia lo utiliza para materializar el puerto de
 * salida del dominio mediante consultas derivadas de Spring Data.
 */
public interface JobOfferJpaRepository extends JpaRepository<JobOfferEntity, Long> {

    /**
     * Comprueba la existencia de una entidad con la URL indicada.
     *
     * @param url URL que se quiere comprobar
     * @return {@code true} si existe una entidad con esa URL
     */
    boolean existsByUrl(String url);

    /**
     * Busca una entidad por su URL.
     *
     * @param url URL de la oferta
     * @return entidad encontrada, o un resultado vacío si no existe
     */
    Optional<JobOfferEntity> findByUrl(String url);

    /**
     * Comprueba la existencia de una entidad con la identidad de fuente e
     * identificador externo indicada.
     *
     * @param source fuente de la oferta
     * @param externalId identificador de la oferta en la fuente
     * @return {@code true} si existe esa identidad
     */
    boolean existsBySourceAndExternalId(String source, String externalId);
}
