CREATE TABLE job_offer_status_history (
    id BIGSERIAL PRIMARY KEY,
    job_offer_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    changed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_job_offer_status_history_job_offer
        FOREIGN KEY (job_offer_id)
        REFERENCES job_offer(id)
);