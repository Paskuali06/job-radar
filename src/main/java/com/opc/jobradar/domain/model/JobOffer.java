package com.opc.jobradar.domain.model;

import java.time.OffsetDateTime;

/**
 * Representa una oferta de empleo dentro del dominio de Job Radar.
 *
 * Mantiene los datos con los que la aplicación identifica, clasifica y
 * consulta una oferta sin depender de detalles de persistencia.
 */
public class JobOffer {

    private Long id;
    private Long userId;
    private String company;
    private String title;
    private String location;
    private String workMode;
    private String url;
    private OffsetDateTime publishedAt;
    private JobOfferStatus status;
    private Integer score;
    private String classification;
    private String description;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String source;
    private String externalId;

    public JobOffer() {
    }

    public JobOffer(
            Long id,
            String company,
            String title,
            String location,
            String workMode,
            String url,
            OffsetDateTime publishedAt,
            JobOfferStatus status,
            Integer score,
            String classification,
            String description,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt,
            String source,
            String externalId
    ) {
        this.id = id;
        this.company = company;
        this.title = title;
        this.location = location;
        this.workMode = workMode;
        this.url = url;
        this.publishedAt = publishedAt;
        this.status = status;
        this.score = score;
        this.classification = classification;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.source = source;
        this.externalId = externalId;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCompany() {
        return company;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getWorkMode() {
        return workMode;
    }

    public String getUrl() {
        return url;
    }

    public OffsetDateTime getPublishedAt() {
        return publishedAt;
    }

    public JobOfferStatus getStatus() {
        return status;
    }

    public Integer getScore() {
        return score;
    }

    public String getClassification() {
        return classification;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getSource() {
        return source;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPublishedAt(OffsetDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setStatus(JobOfferStatus status) {
        this.status = status;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}