package com.opc.jobradar.domain.port.out;

import com.opc.jobradar.domain.model.JobOffer;

import java.util.Optional;

public interface JobOfferRepository {

    JobOffer save(JobOffer jobOffer);

    Optional<JobOffer> findById(Long id);

    Optional<JobOffer> findByUrl(String url);

    boolean existsByUrl(String url);

    boolean existsBySourceAndExternalId(String source, String externalId);
    
}
