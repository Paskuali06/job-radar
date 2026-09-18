package com.opc.jobradar.domain.model;

/**
 * Representa los filtros opcionales para consultar ofertas de empleo.
 *
 * Permite filtrar por empresa, ubicación, modalidad de trabajo y estado,
 * además de realizar búsquedas textuales y ordenar los resultados.
 */
public class FiltersByJobOffer {

    private String company;
    private String location;
    private String workMode;
    private JobOfferStatus status;
    private String search;
    private String sortBy;
    private String sortDirection;

    public FiltersByJobOffer() {
    }

    public FiltersByJobOffer(
            String company,
            String location,
            String workMode,
            JobOfferStatus status
    ) {
        this.company = company;
        this.location = location;
        this.workMode = workMode;
        this.status = status;
    }

    public FiltersByJobOffer(
            String company,
            String location,
            String workMode,
            JobOfferStatus status,
            String search,
            String sortBy,
            String sortDirection
    ) {
        this.company = company;
        this.location = location;
        this.workMode = workMode;
        this.status = status;
        this.search = search;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public JobOfferStatus getStatus() {
        return status;
    }

    public void setStatus(JobOfferStatus status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}