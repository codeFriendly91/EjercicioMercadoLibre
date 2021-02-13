package com.mercado.rest.repository;

import com.mercado.rest.dto.traceService.TraceResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraceRepository extends MongoRepository<TraceResponse, Integer> {

    TraceResponse findByIsoCode (String isoCode);
    boolean existsByIsoCode(String isoCode);

}
