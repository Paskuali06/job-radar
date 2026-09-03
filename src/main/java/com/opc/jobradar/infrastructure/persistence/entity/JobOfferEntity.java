package com.opc.jobradar.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "job_offer",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_job_offer_url", columnNames = "url")
        }
)
public class JobOfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String company;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 200)
    private String location;

    @Column(name = "work_mode", length = 50)
    private String workMode;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false, length = 100)
    private String source;

    @Column(name = "external_id", nullable = false, length = 100)
    private String externalId;

    @Column(name = "published_at")
    private OffsetDateTime publishedAt;

    @Column(nullable = false, length = 30)
    private String status;

    private Integer score;

    @Column(length = 1)
    private String classification;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

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

    public String getSource() {
        return source;
    }

    public String getExternalId() {
        return externalId;
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

    public void setSource(String source) {
        this.source = source;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
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