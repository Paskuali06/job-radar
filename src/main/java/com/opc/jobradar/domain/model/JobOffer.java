package com.opc.jobradar.domain.model;

import java.time.OffsetDateTime;

public class JobOffer {

    private Long id;
    private String company;
    private String title;
    private String location;
    private String workMode;
    private String url;
    private OffsetDateTime publishedAt;
    private String status;
    private Integer score;
    private String classification;
    private String description;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

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
            String status,
            Integer score,
            String classification,
            String description,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
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
    }

    public Long getId() {
        return id;
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

    public String getStatus() {
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

    public void setId(Long id) {
        this.id = id;
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

    public void setStatus(String status) {
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
}