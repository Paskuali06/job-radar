package com.opc.jobradar.application.service;

import com.opc.jobradar.domain.model.FiltersByJobOffer;
import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso que consulta las ofertas de empleo almacenadas.
 */
@Service
public class GetJobOffersService {

    private final JobOfferRepository jobOfferRepository;

    public GetJobOffersService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    /**
     * Consulta las ofertas de empleo de un usuario aplicando los filtros
     * proporcionados.
     *
     * @param userId identificador del usuario propietario
     * @param filters filtros opcionales de consulta
     * @return lista de ofertas del usuario que cumplen los filtros
     */
    public List<JobOffer> get(
            Long userId,
            FiltersByJobOffer filters
    ) {
        FiltersByJobOffer normalizedFilters = new FiltersByJobOffer(
                normalize(filters == null ? null : filters.getCompany()),
                normalize(filters == null ? null : filters.getLocation()),
                normalize(filters == null ? null : filters.getWorkMode()),
                filters == null ? null : filters.getStatus()
        );

        normalizedFilters.setSearch(
                normalize(filters == null ? null : filters.getSearch())
        );

        normalizedFilters.setSortBy(
                filters == null ? null : filters.getSortBy()
        );

        normalizedFilters.setSortDirection(
                filters == null ? null : filters.getSortDirection()
        );
        
        normalizedFilters.setPage(
        filters == null ? null : filters.getPage()
        );

        normalizedFilters.setSize(
        filters == null ? null : filters.getSize()
        );

        return jobOfferRepository.findAll(
                userId,
                normalizedFilters
        );
    }

    /**
     * Convierte un filtro de texto vacío en ausencia de filtro.
     *
     * @param value valor del filtro
     * @return {@code null} si el valor está vacío o es {@code null};
     *         el valor original en caso contrario
     */
    private String normalize(String value) {
        return value == null || value.isEmpty() ? null : value;
    }
}