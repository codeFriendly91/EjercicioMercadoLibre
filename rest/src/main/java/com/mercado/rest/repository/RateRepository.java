package com.mercado.rest.repository;

import com.mercado.rest.document.Rate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends MongoRepository <Rate, Integer> {

    Rate findByCurrency (String currency);

}
