package com.opc.jobradar.infrastructure.persistence.repository;

import com.opc.jobradar.infrastructure.persistence.entity.JobOfferEntity;
import org.springframework.data.domain.Pageable;
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

    boolean existsByUrl(String url);

    Optional<JobOfferEntity> findByUrl(String url);

    boolean existsByUserIdAndSourceAndExternalId(
            Long userId,
            String source,
            String externalId
    );

    Optional<JobOfferEntity> findByIdAndUserId(Long id, Long userId);

    long countByUserId(Long userId);

    long countByUserIdAndStatus(
            Long userId,
            String status
    );

    /**
     * Busca las ofertas de un usuario aplicando los filtros proporcionados,
     * la ordenación solicitada y, cuando se proporciona, la paginación.
     *
     * @param userId identificador del usuario propietario
     * @param company empresa de la oferta
     * @param location ubicación de la oferta
     * @param workMode modalidad de trabajo
     * @param status estado de la oferta
     * @param search texto de búsqueda sobre empresa, título o descripción
     * @param sortBy campo por el que ordenar
     * @param sortDirection dirección de ordenación
     * @param pageable configuración de paginación
     * @return ofertas del usuario que cumplen los filtros proporcionados
     */
    @Query("""
        SELECT jobOffer
        FROM JobOfferEntity jobOffer
        WHERE jobOffer.userId = :userId
          AND (:company IS NULL OR jobOffer.company = :company)
          AND (:location IS NULL OR jobOffer.location = :location)
          AND (:workMode IS NULL OR jobOffer.workMode = :workMode)
          AND (:status IS NULL OR jobOffer.status = :status)
          AND (
                COALESCE(:search, '') = ''
                OR LOWER(jobOffer.company) LIKE CONCAT('%', LOWER(COALESCE(:search, '')), '%')
                OR LOWER(jobOffer.title) LIKE CONCAT('%', LOWER(COALESCE(:search, '')), '%')
                OR LOWER(jobOffer.description) LIKE CONCAT('%', LOWER(COALESCE(:search, '')), '%')
          )
        ORDER BY
            CASE
                WHEN :sortBy = 'createdAt'
                     AND :sortDirection = 'asc'
                THEN jobOffer.createdAt
            END ASC,
            CASE
                WHEN :sortBy = 'createdAt'
                     AND :sortDirection = 'desc'
                THEN jobOffer.createdAt
            END DESC,
            CASE
                WHEN :sortBy = 'updatedAt'
                     AND :sortDirection = 'asc'
                THEN jobOffer.updatedAt
            END ASC,
            CASE
                WHEN :sortBy = 'updatedAt'
                     AND :sortDirection = 'desc'
                THEN jobOffer.updatedAt
            END DESC,
            CASE
                WHEN :sortBy = 'publishedAt'
                     AND :sortDirection = 'asc'
                THEN jobOffer.publishedAt
            END ASC,
            CASE
                WHEN :sortBy = 'publishedAt'
                     AND :sortDirection = 'desc'
                THEN jobOffer.publishedAt
            END DESC
        """)
    List<JobOfferEntity> findAll(
            @Param("userId") Long userId,
            @Param("company") String company,
            @Param("location") String location,
            @Param("workMode") String workMode,
            @Param("status") String status,
            @Param("search") String search,
            @Param("sortBy") String sortBy,
            @Param("sortDirection") String sortDirection,
            Pageable pageable
    );
}