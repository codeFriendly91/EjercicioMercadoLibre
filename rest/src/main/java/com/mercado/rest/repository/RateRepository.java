package com.mercado.rest.repository;

import com.mercado.rest.document.Rate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateRepository extends MongoRepository <Rate, Integer> {


}
