ALTER TABLE job_offer
ADD COLUMN user_id BIGINT NOT NULL;

ALTER TABLE job_offer
ADD CONSTRAINT fk_job_offer_user
FOREIGN KEY (user_id)
REFERENCES app_user(id);

DROP INDEX uk_job_offer_source_external_id;

CREATE UNIQUE INDEX uk_job_offer_user_source_external_id
    ON job_offer (user_id, source, external_id);