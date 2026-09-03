ALTER TABLE job_offer
ADD COLUMN source VARCHAR(100) NOT NULL,
ADD COLUMN external_id VARCHAR(100) NOT NULL;

DROP INDEX uk_job_offer_url;

CREATE UNIQUE INDEX uk_job_offer_source_external_id
    ON job_offer (source, external_id);