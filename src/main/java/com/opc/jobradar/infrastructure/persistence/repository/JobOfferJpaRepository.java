package com.opc.jobradar.infrastructure.persistence.repository;

import com.opc.jobradar.infrastructure.persistence.entity.JobOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    /**
     * Busca ofertas aplicando únicamente los filtros que tengan valor.
     *
     * @param company empresa de la oferta
     * @param location ubicación de la oferta
     * @param workMode modalidad de trabajo
     * @param status estado de la oferta
     * @return ofertas que cumplen todos los filtros proporcionados
     */
    @Query("""
            SELECT jobOffer
            FROM JobOfferEntity jobOffer
            WHERE (:company IS NULL OR jobOffer.company = :company)
              AND (:location IS NULL OR jobOffer.location = :location)
              AND (:workMode IS NULL OR jobOffer.workMode = :workMode)
              AND (:status IS NULL OR jobOffer.status = :status)
            """)
    List<JobOfferEntity> findAll(
            @Param("company") String company,
            @Param("location") String location,
            @Param("workMode") String workMode,
            @Param("status") String status
    );
}