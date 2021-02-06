package com.mercado.rest.repository;

import com.mercado.rest.document.Rate;
import com.mercado.rest.dto.trace.response.TraceResponse;
import com.sun.deploy.trace.Trace;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TraceRepository extends MongoRepository<TraceResponse, Integer> {

    TraceResponse findByIsoCode (String isoCode);

}
