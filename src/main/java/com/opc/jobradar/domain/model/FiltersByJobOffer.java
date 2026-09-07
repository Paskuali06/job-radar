package com.opc.jobradar.domain.model;

import com.opc.jobradar.domain.model.JobOfferStatus;

/**
 * Representa los filtros opcionales para consultar ofertas de empleo.
 *
 * Permite filtrar por empresa, ubicación, modalidad de trabajo y estado.
 */
public class FiltersByJobOffer {

    private String company;
    private String location;
    private String workMode;
    private JobOfferStatus status;

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
}