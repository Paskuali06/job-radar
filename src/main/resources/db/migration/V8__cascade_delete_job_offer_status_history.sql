ALTER TABLE job_offer_status_history
DROP CONSTRAINT fk_job_offer_status_history_job_offer;

ALTER TABLE job_offer_status_history
ADD CONSTRAINT fk_job_offer_status_history_job_offer
FOREIGN KEY (job_offer_id)
REFERENCES job_offer(id)
ON DELETE CASCADE;