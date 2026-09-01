CREATE TABLE job_offer ( 
    id BIGSERIAL PRIMARY KEY,

    company VARCHAR(150) NOT NULL,
    title VARCHAR(200) NOT NULL,

    location VARCHAR(200),
    work_mode VARCHAR(50),

    url TEXT NOT NULL,

    published_at TIMESTAMP WITH TIME ZONE,

    status VARCHAR(30) NOT NULL,

    score INTEGER,

    classification VARCHAR(1),

    description TEXT,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX uk_job_offer_url
    ON job_offer (url);